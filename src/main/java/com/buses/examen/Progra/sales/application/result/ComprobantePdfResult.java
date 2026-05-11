package com.buses.examen.Progra.sales.application.result;

/**
 * Resultado de descarga de comprobante en PDF.
 *
 * @param fileName nombre sugerido del archivo
 * @param contentBytes bytes del PDF
 */
public record ComprobantePdfResult(
        String fileName,
        byte[] contentBytes) {
}
