package org.jetbrains.jet.j2k.ast;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jet.j2k.util.AstUtil;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.jetbrains.jet.j2k.util.AstUtil.*;

/**
 * @author ignatov
 */
public class Class extends Member {
  String TYPE = "class";
  final Identifier myName;
  private final List<Expression> myBaseClassParams;
  private final List<Initializer> myInitializers;
  private final List<Element> myTypeParameters;
  private final List<Type> myExtendsTypes;
  private final List<Type> myImplementsTypes;
  final List<Class> myInnerClasses;
  private final List<Function> myMethods;
  final List<Field> myFields;

  public Class(Identifier name, Set<String> modifiers, List<Element> typeParameters, List<Type> extendsTypes,
               List<Expression> baseClassParams, List<Type> implementsTypes, List<Class> innerClasses, List<Function> methods, List<Field> fields, List<Initializer> initializers) {
    myName = name;
    myBaseClassParams = baseClassParams;
    myModifiers = modifiers;
    myTypeParameters = typeParameters;
    myExtendsTypes = extendsTypes;
    myImplementsTypes = implementsTypes;
    myInnerClasses = innerClasses;
    myMethods = methods;
    myFields = fields;
    myInitializers = initializers;
  }

  @Nullable
  private Constructor getPrimaryConstructor() {
    for (Function m : myMethods)
      if (m.getKind() == Kind.CONSTRUCTOR)
        if (((Constructor) m).isPrimary())
          return (Constructor) m;
    return null;
  }

  String primaryConstructorSignatureToKotlin() {
    Constructor maybeConstructor = getPrimaryConstructor();
    if (maybeConstructor != null)
      return maybeConstructor.primarySignatureToKotlin();
    return "(" + ")";
  }

  String primaryConstructorBodyToKotlin() {
    Constructor maybeConstructor = getPrimaryConstructor();
    if (maybeConstructor != null && !maybeConstructor.getBlock().isEmpty())
      return maybeConstructor.primaryBodyToKotlin();
    return EMPTY;
  }

  private boolean hasWhere() {
    for (Element t : myTypeParameters)
      if (t instanceof TypeParameter && ((TypeParameter) t).hasWhere())
        return true;
    return false;
  }

  String typeParameterWhereToKotlin() {
    if (hasWhere()) {
      List<String> wheres = new LinkedList<String>();
      for (Element t : myTypeParameters)
        if (t instanceof TypeParameter)
          wheres.add(((TypeParameter) t).getWhereToKotlin());
      return SPACE + "where" + SPACE + join(wheres, COMMA_WITH_SPACE) + SPACE;
    }
    return EMPTY;
  }

  @NotNull
  List<Function> methodsExceptConstructors() {
    final LinkedList<Function> result = new LinkedList<Function>();
    for (Function m : myMethods)
      if (m.getKind() != Kind.CONSTRUCTOR)
        result.add(m);
    return result;
  }

  @NotNull
  List<Function> secondaryConstructorsAsStaticInitFunction() {
    final LinkedList<Function> result = new LinkedList<Function>();
    for (Function m : myMethods)
      if (m.getKind() == Kind.CONSTRUCTOR && !((Constructor) m).isPrimary()) {
        Set<String> modifiers = new HashSet<String>(m.myModifiers);
        modifiers.add(Modifier.STATIC);

        final List<Statement> statements = m.getBlock().getStatements();
        statements.add(new ReturnStatement(new IdentifierImpl("__"))); // TODO: move to one place, find other __ usages
        final Block block = new Block(statements);

        result.add(new Function(
          new IdentifierImpl("init"),
          modifiers,
          new ClassType(myName),
          m.getTypeParameters(),
          m.getParams(),
          block
        ));
      }
    return result;
  }

  String typeParametersToKotlin() {
    return myTypeParameters.size() > 0 ? "<" + AstUtil.joinNodes(myTypeParameters, COMMA_WITH_SPACE) + ">" : EMPTY;
  }

  List<String> baseClassSignatureWithParams() {
    if (TYPE.equals("class") && myExtendsTypes.size() == 1) {
      LinkedList<String> result = new LinkedList<String>();
      result.add(myExtendsTypes.get(0).toKotlin() + "(" + joinNodes(myBaseClassParams, COMMA_WITH_SPACE) + ")");
      return result;
    } else
      return nodesToKotlin(myExtendsTypes);
  }

  String implementTypesToKotlin() {
    List<String> allTypes = new LinkedList<String>() {
      {
        addAll(baseClassSignatureWithParams());
        addAll(nodesToKotlin(myImplementsTypes));
      }
    };
    return allTypes.size() == 0 ? EMPTY : SPACE + COLON + SPACE + join(allTypes, COMMA_WITH_SPACE);
  }

  String modifiersToKotlin() {
    List<String> modifierList = new LinkedList<String>();

    if (needAbstractModifier())
      modifierList.add(Modifier.ABSTRACT);

    modifierList.add(accessModifier());

    if (needOpenModifier())
      modifierList.add(Modifier.OPEN);

    if (modifierList.size() > 0)
      return join(modifierList, SPACE) + SPACE;

    return EMPTY;
  }

  boolean needOpenModifier() {
    return !myModifiers.contains(Modifier.FINAL);
  }

  boolean needAbstractModifier() {
    return isAbstract();
  }

  String bodyToKotlin() {
    return SPACE + "{" + N +
      AstUtil.joinNodes(getNonStatic(myFields), N) + N +
      AstUtil.joinNodes(getNonStatic(myInitializers), N) + N +
      classObjectToKotlin() + N +
      AstUtil.joinNodes(getNonStatic(methodsExceptConstructors()), N) + N +
      AstUtil.joinNodes(getNonStatic(myInnerClasses), N) + N +
      primaryConstructorBodyToKotlin() + N +
      "}";
  }

  private List<Member> getStatic(List<? extends Member> members) {
    List<Member> result = new LinkedList<Member>();
    for (Member m : members)
      if (m.isStatic())
        result.add(m);
    return result;
  }

  private List<Member> getNonStatic(List<? extends Member> members) {
    List<Member> result = new LinkedList<Member>();
    for (Member m : members)
      if (!m.isStatic())
        result.add(m);
    return result;
  }

  private String classObjectToKotlin() {
    final List<Member> staticMethods = new LinkedList<Member>(secondaryConstructorsAsStaticInitFunction());
    staticMethods.addAll(getStatic(methodsExceptConstructors()));
    final List<Member> staticFields = getStatic(myFields);
    final List<Member> staticInitializers = getStatic(myInitializers);
    final List<Member> staticInnerClasses = getStatic(myInnerClasses);
    if (staticFields.size() + staticMethods.size() + staticInnerClasses.size() + staticInitializers.size() > 0) {
      return "class" + SPACE + "object" + SPACE + "{" + N +
        AstUtil.joinNodes(staticFields, N) + N +
        AstUtil.joinNodes(staticInitializers, N) + N +
        AstUtil.joinNodes(staticMethods, N) + N +
        AstUtil.joinNodes(staticInnerClasses, N) + N +
        "}";
    }
    return EMPTY;
  }

  @NotNull
  @Override
  public String toKotlin() {
    return modifiersToKotlin() + TYPE + SPACE + myName.toKotlin() + typeParametersToKotlin() + primaryConstructorSignatureToKotlin() +
      implementTypesToKotlin() +
      typeParameterWhereToKotlin() +
      bodyToKotlin();
  }
}