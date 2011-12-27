package pack

import std.compatibility.*

fun main(args : Array<String>) {
    System.out?.println(
        EstatisticasSimples.calculaQuantidade(doubleArray(.0, .1, .3, .4))
    )
}

public open class EstatisticasSimples() {
    class object {
        open public fun calculaValorMinimo(valoresDeEntrada : DoubleArray?) : Double {
            var valorMinimoCalculado : Double = valoresDeEntrada[0]
            for (i in 0..(valoresDeEntrada?.size - 1.int)) {
                if ((valoresDeEntrada[i] < valorMinimoCalculado))
                {
                    valorMinimoCalculado = valoresDeEntrada[i]
                }

            }
            return valorMinimoCalculado
        }
        open public fun calculaValorMaximo(valoresDeEntrada : DoubleArray?) : Double {
            var valorMaximoCalculado : Double = valoresDeEntrada[0]
            for (i in 0..(valoresDeEntrada?.size - 1.int)) {
                if ((valoresDeEntrada[i] > valorMaximoCalculado))
                {
                    valorMaximoCalculado = valoresDeEntrada[i]
                }

            }
            return valorMaximoCalculado
        }
        open public fun calculaQuantidade(valoresDeEntrada : DoubleArray?) : Int {
            var quantidadeDeValores : Int = valoresDeEntrada?.size.sure()
            return quantidadeDeValores
        }
        open public fun calculaMedia(valoresDeEntrada : DoubleArray?) : Double {
            var somaDosValores : Double = 0.dbl
            for (i in 0..(valoresDeEntrada?.size - 1.int)) {
                somaDosValores = (somaDosValores + valoresDeEntrada[i])
            }
            return (somaDosValores / valoresDeEntrada?.size)
        }
        open public fun calcula(valoresDeEntrada : DoubleArray?) : DoubleArray? {
            var valoresDeSaida : DoubleArray? = doubleArray(0.dbl, 0.dbl, 0.dbl, 0.dbl)
            valoresDeSaida[0] = calculaValorMinimo(valoresDeEntrada)
            valoresDeSaida[1] = calculaValorMaximo(valoresDeEntrada)
            valoresDeSaida[2] = calculaQuantidade(valoresDeEntrada).dbl
            valoresDeSaida[3] = calculaMedia(valoresDeEntrada)
            return valoresDeSaida
        }
    }
}
