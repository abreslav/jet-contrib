package pack

import junit.framework.Assert
import junit.framework.TestCase
import std.compatibility.*

public open class EstatisticasSimplesTest() : TestCase("estName") {
    open public fun testDeveCalcularValorMinimoEntreDoisValores() : Unit {
        var valoresDeEntrada : DoubleArray? = doubleArray(9.dbl, 5.dbl)
        var valorMinimoCalculado : Double = EstatisticasSimples.calculaValorMinimo(valoresDeEntrada)
        Assert.assertEquals(5.dbl, valorMinimoCalculado, 0.dbl)
    }
    open public fun testDeveCalcularValorMinimoEntreTresValores() : Unit {
        var valoresDeEntrada : DoubleArray? = doubleArray(9.dbl, 5.dbl, 2.dbl)
        var valorMinimoCalculado : Double = EstatisticasSimples.calculaValorMinimo(valoresDeEntrada)
        Assert.assertEquals(2.dbl, valorMinimoCalculado, 0.dbl)
    }
    open public fun testdeveCalcularValorMinimoEntreQuatroValores() : Unit {
        var valoresDeEntrada : DoubleArray? = doubleArray(9.dbl, 5.dbl, 2.dbl, 15.dbl)
        var valorMinimoCalculado : Double = EstatisticasSimples.calculaValorMinimo(valoresDeEntrada)
        Assert.assertEquals(2.dbl, valorMinimoCalculado, 0.dbl)
    }
    open public fun testdeveCalcularValorMinimoEntreCincoValores() : Unit {
        var valoresDeEntrada : DoubleArray? = doubleArray(9.dbl, 5.dbl, 2.dbl, 15.dbl, 1.98)
        var valorMinimoCalculado : Double = EstatisticasSimples.calculaValorMinimo(valoresDeEntrada)
        Assert.assertEquals(1.98, valorMinimoCalculado, 0.dbl)
    }
    open public fun testdeveCalcularValorMaximoEntreDoisValores() : Unit {
        var valoresDeEntrada : DoubleArray? = doubleArray(9.dbl, 5.dbl)
        var valorMaximoCalculado : Double = EstatisticasSimples.calculaValorMaximo(valoresDeEntrada)
        Assert.assertEquals(9.dbl, valorMaximoCalculado, 0.dbl)
    }
    open public fun testdeveCalcularValorMaximoEntreTresValores() : Unit {
        var valoresDeEntrada : DoubleArray? = doubleArray(9.dbl, 50.dbl, 2.dbl)
        var valorMaximoCalculado : Double = EstatisticasSimples.calculaValorMaximo(valoresDeEntrada)
        Assert.assertEquals(50.dbl, valorMaximoCalculado, 0.dbl)
    }
    open public fun testdeveCalcularValorMaximoEntreQuatroValores() : Unit {
        var valoresDeEntrada : DoubleArray? = doubleArray(9.dbl, 5.dbl, 22.dbl, 15.dbl)
        var valorMaximoCalculado : Double = EstatisticasSimples.calculaValorMaximo(valoresDeEntrada)
        Assert.assertEquals(22.dbl, valorMaximoCalculado, 0.dbl)
    }
    open public fun testdeveCalcularValorMaximoEntreCincoValores() : Unit {
        var valoresDeEntrada : DoubleArray? = doubleArray(9.dbl, 5.dbl, 2.dbl, 15.dbl, 15.01)
        var valorMaximoCalculado : Double = EstatisticasSimples.calculaValorMaximo(valoresDeEntrada)
        Assert.assertEquals(15.01, valorMaximoCalculado, 0.dbl)
    }
    open public fun testdeveCalcularQuantidadeDeValores() : Unit {
        var valoresDeEntrada : DoubleArray? = doubleArray(9.dbl, 5.dbl)
        var quantidadeDeValores : Int = EstatisticasSimples.calculaQuantidade(valoresDeEntrada)
        Assert.assertEquals(2, quantidadeDeValores)
    }
    open public fun testdeveCalcularQuantidadeDeValoresTest2() : Unit {
        var valoresDeEntrada : DoubleArray? = doubleArray(9.dbl, 5.dbl, 8.dbl)
        var quantidadeDeValores : Int = EstatisticasSimples.calculaQuantidade(valoresDeEntrada)
        Assert.assertEquals(3, quantidadeDeValores)
    }
    open public fun testdeveCalcularQuantidadeDeValoresTest3() : Unit {
        var valoresDeEntrada : DoubleArray? = doubleArray(9.dbl, 5.dbl, 8.dbl, 9.dbl)
        var quantidadeDeValores : Int = EstatisticasSimples.calculaQuantidade(valoresDeEntrada)
        Assert.assertEquals(4, quantidadeDeValores)
    }
    open public fun testdeveCalcularQuantidadeDeValoresTest4() : Unit {
        var valoresDeEntrada : DoubleArray? = doubleArray(9.dbl, 5.dbl, 8.dbl, 9.dbl, 7.dbl)
        var quantidadeDeValores : Int = EstatisticasSimples.calculaQuantidade(valoresDeEntrada)
        Assert.assertEquals(5, quantidadeDeValores)
    }
    open public fun testdeveCalcularMediaDeDoisValores() : Unit {
        var valoresDeEntrada : DoubleArray? = doubleArray(9.dbl, 5.dbl)
        var mediaDeValores : Double = EstatisticasSimples.calculaMedia(valoresDeEntrada)
        Assert.assertEquals(7.dbl, mediaDeValores, 0.dbl)
    }
    open public fun testdeveCalcularMediaDeTresValores() : Unit {
        var valoresDeEntrada : DoubleArray? = doubleArray(9.dbl, 5.dbl, 4.dbl)
        var mediaDeValores : Double = EstatisticasSimples.calculaMedia(valoresDeEntrada)
        Assert.assertEquals(6.dbl, mediaDeValores, 0.dbl)
    }
    open public fun testdeveCalcularMediaDeQuatroValores() : Unit {
        var valoresDeEntrada : DoubleArray? = doubleArray(8.dbl, 4.dbl, 6.dbl, 6.dbl)
        var mediaDeValores : Double = EstatisticasSimples.calculaMedia(valoresDeEntrada)
        Assert.assertEquals(6.dbl, mediaDeValores, 0.dbl)
    }
    open public fun testdeveCalcularMediaDeCincoValores() : Unit {
        var valoresDeEntrada : DoubleArray? = doubleArray(10.dbl, 5.dbl, 5.dbl, 10.dbl, 0.dbl)
        var mediaDeValores : Double = EstatisticasSimples.calculaMedia(valoresDeEntrada)
        Assert.assertEquals(6.dbl, mediaDeValores, 0.dbl)
    }
}
