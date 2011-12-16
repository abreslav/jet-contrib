/*
 * @author alex.tkachman
 */
package jet;

import jet.typeinfo.TypeInfo;
public abstract class ExtensionFunction22<E, D1, D2, D3, D4, D5, D6, D7, D8, D9, D10, D11, D12, D13, D14, D15, D16, D17, D18, D19, D20, D21, D22, R> extends DefaultJetObject {
    protected ExtensionFunction22(TypeInfo<?> typeInfo) {
        super(typeInfo);
    }

    public abstract R invoke(E receiver, D1 d1, D2 d2, D3 d3, D4 d4, D5 d5, D6 d6, D7 d7, D8 d8, D9 d9, D10 d10, D11 d11, D12 d12, D13 d13, D14 d14, D15 d15, D16 d16, D17 d17, D18 d18, D19 d19, D20 d20, D21 d21, D22 d22);

    @Override
    public String toString() {
      return "{E.(d1: D1, d2: D2, d3: D3, d4: D4, d5: D5, d6: D6, d7: D7, d8: D8, d9: D9, d10: D10, d11: D11, d12: D12, d13: D13, d14: D14, d15: D15, d16: D16, d17: D17, d18: D18, d19: D19, d20: D20, d21: D21, d22: D22) : R)}";
    }
}

