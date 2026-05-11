package com.buses.examen.Progra.sales.adapter.out.pdf;

import com.buses.examen.Progra.sales.application.port.out.ComprobantePdfPort;
import com.buses.examen.Progra.sales.domain.Compra;
import com.buses.examen.Progra.sales.domain.Comprobante;
import org.springframework.stereotype.Component;

/**
 * Adaptador temporal que representa la solicitud de generación PDF.
 */
@Component
public class NoOpComprobantePdfAdapter implements ComprobantePdfPort {

    /** {@inheritDoc} */
    @Override
    public void generateFor(final Compra compra, final Comprobante comprobante) {
        // Adaptador intencionalmente vacío para mantener desacoplado el puerto de PDF
        // cuando la infraestructura de generación electrónica no está instalada.
    }

    /** {@inheritDoc} */
    @Override
    public byte[] renderFor(final Compra compra, final Comprobante comprobante) {
        final String minimalPdf = "%PDF-1.4\n"
                + "1 0 obj<<>>endobj\n"
                + "2 0 obj<< /Type /Catalog /Pages 3 0 R >>endobj\n"
                + "3 0 obj<< /Type /Pages /Kids [4 0 R] /Count 1 >>endobj\n"
                + "4 0 obj<< /Type /Page /Parent 3 0 R /MediaBox [0 0 595 842] /Contents 5 0 R /Resources<<>> >>endobj\n"
                + "5 0 obj<< /Length 0 >>stream\nendstream\nendobj\n"
                + "xref\n0 6\n0000000000 65535 f \n"
                + "0000000010 00000 n \n0000000030 00000 n \n0000000080 00000 n \n"
                + "0000000145 00000 n \n0000000250 00000 n \n"
                + "trailer<< /Root 2 0 R /Size 6 >>\nstartxref\n300\n%%EOF";
        return minimalPdf.getBytes(java.nio.charset.StandardCharsets.US_ASCII);
    }
}
