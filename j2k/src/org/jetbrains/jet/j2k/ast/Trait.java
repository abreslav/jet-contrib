package org.jetbrains.jet.j2k.ast;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

/**
 * @author ignatov
 */
public class Trait extends Class {
  public Trait(Identifier name, Set<String> modifiers, List<Element> typeParameters, List<Type> extendsTypes, List<Type> implementsTypes, List<Class> innerClasses, List<Function> methods, List<Field> fields) {
    super(name, modifiers, typeParameters, extendsTypes, implementsTypes, innerClasses, methods, fields);
    TYPE = "trait";
  }

  @Override
  String primaryConstructorSignatureToKotlin() {
    return EMPTY;
  }

  boolean needOpenModifier() {
    return false;
  }
}