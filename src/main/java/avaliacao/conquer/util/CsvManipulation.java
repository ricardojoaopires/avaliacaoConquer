package avaliacao.conquer.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import avaliacao.conquer.model.Gasto;

/**
 * Classe responsável pela criação e manipulação do arquivo CSV que será
 * exportado.
 * 
 * @author ricardo
 */
public class CsvManipulation {

	private static final String HEADER = "MUNICIPIO, NRO_BENEFICIADOS, GASTO, ANO_INIC, MES_INIC, ANO_FIM, MES_FIM";

	public CsvManipulation(final PrintWriter pw) throws IOException {
		pw.write(HEADER + "\n");
	}

	/**
	 * Método responsável pela escrita do conteúdo no arquivo CSV.
	 * 
	 * @param pw     @{@link PrintWriter}
	 * @param gastos : os gastos que serão adicionados ao arquivo CSV.
	 * @throws IOException @{@link IOException}
	 */
	public void write(final PrintWriter pw, final List<Gasto> gastos) throws IOException {
		for (Gasto gasto : gastos) {
			pw.write(gasto.getInfosGastoParaExportacao() + "\n");
		}
	}
}
