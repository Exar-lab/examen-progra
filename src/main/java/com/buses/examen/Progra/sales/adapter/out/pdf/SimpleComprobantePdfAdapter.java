package com.buses.examen.Progra.sales.adapter.out.pdf;

import com.buses.examen.Progra.customer.domain.Cliente;
import com.buses.examen.Progra.sales.application.port.out.ComprobantePdfPort;
import com.buses.examen.Progra.sales.domain.Compra;
import com.buses.examen.Progra.sales.domain.Comprobante;
import com.buses.examen.Progra.sales.domain.Ticket;
import com.buses.examen.Progra.service.domain.Servicio;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Adaptador PDF simple para emitir comprobantes descargables sin depender de librerías externas.
 */
@Component
public class SimpleComprobantePdfAdapter implements ComprobantePdfPort {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /** {@inheritDoc} */
    @Override
    public void generateFor(final Compra compra, final Comprobante comprobante) {
        // El PDF se renderiza bajo demanda en renderFor para evitar almacenar binarios en base de datos.
    }

    /** {@inheritDoc} */
    @Override
    public byte[] renderFor(final Compra compra, final Comprobante comprobante) {
        final List<String> lines = buildReceiptLines(compra, comprobante);
        return renderPdf(lines);
    }

    private List<String> buildReceiptLines(final Compra compra, final Comprobante comprobante) {
        final Cliente cliente = compra.getCliente();
        final List<String> lines = new ArrayList<>();
        lines.add("CENTROBUS - COMPROBANTE ELECTRONICO");
        lines.add("Comprobante: " + comprobante.getSerie() + "-" + comprobante.getNumero());
        lines.add("Tipo: " + comprobante.getTipo());
        lines.add("Fecha emision: " + DATE_TIME_FORMATTER.format(comprobante.getFechaEmision()));
        lines.add("Cliente: " + cliente.getNombres() + " " + cliente.getApellidos());
        lines.add("Email: " + cliente.getEmail());
        lines.add("Fecha compra: " + DATE_TIME_FORMATTER.format(compra.getFechaCompra()));
        lines.add(" ");
        lines.add("TICKETS");

        for (final Ticket ticket : compra.getTickets()) {
            appendTicketLines(lines, ticket);
        }

        lines.add(" ");
        lines.add("Total: " + comprobante.getMoneda() + " " + formatMoney(comprobante.getMontoTotal()));
        lines.add("Presente este comprobante y el codigo de ticket al abordar el bus.");
        return lines;
    }

    private void appendTicketLines(final List<String> lines, final Ticket ticket) {
        final Servicio servicio = ticket.getServicio();
        lines.add("Codigo ticket: " + ticket.getCodigoTicket());
        lines.add("Ruta: " + servicio.getRuta().getCiudadOrigen().getPais().getNombre()
                + " -> " + servicio.getRuta().getCiudadDestino().getPais().getNombre());
        lines.add("Salida: " + DATE_TIME_FORMATTER.format(servicio.getSalidaProgramada()));
        lines.add("Precio: " + formatMoney(ticket.getPrecioFinal()));
        lines.add(" ");
    }

    private byte[] renderPdf(final List<String> lines) {
        final String content = buildContentStream(lines);
        final byte[] contentBytes = content.getBytes(StandardCharsets.US_ASCII);

        final List<byte[]> objects = List.of(
                ascii("<< /Type /Catalog /Pages 2 0 R >>"),
                ascii("<< /Type /Pages /Kids [3 0 R] /Count 1 >>"),
                ascii("<< /Type /Page /Parent 2 0 R /MediaBox [0 0 595 842] /Resources << /Font << /F1 4 0 R >> >> /Contents 5 0 R >>"),
                ascii("<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica >>"),
                ascii("<< /Length " + contentBytes.length + " >>\nstream\n" + content + "endstream")
        );

        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        writeAscii(output, "%PDF-1.4\n");
        final List<Integer> offsets = new ArrayList<>();

        for (int index = 0; index < objects.size(); index++) {
            offsets.add(output.size());
            writeAscii(output, (index + 1) + " 0 obj\n");
            output.writeBytes(objects.get(index));
            writeAscii(output, "\nendobj\n");
        }

        final int xrefOffset = output.size();
        writeAscii(output, "xref\n0 " + (objects.size() + 1) + "\n");
        writeAscii(output, "0000000000 65535 f \n");
        for (final int offset : offsets) {
            writeAscii(output, String.format("%010d 00000 n \n", offset));
        }
        writeAscii(output, "trailer\n<< /Root 1 0 R /Size " + (objects.size() + 1) + " >>\n");
        writeAscii(output, "startxref\n" + xrefOffset + "\n%%EOF");
        return output.toByteArray();
    }

    private String buildContentStream(final List<String> lines) {
        final StringBuilder content = new StringBuilder("BT\n/F1 16 Tf\n50 790 Td\n");
        for (final String line : lines) {
            content.append("(").append(escapePdfText(toAscii(line))).append(") Tj\n0 -22 Td\n");
        }
        content.append("ET\n");
        return content.toString();
    }

    private String toAscii(final String value) {
        return value == null ? "" : value.replaceAll("[^\\x20-\\x7E]", " ");
    }

    private String escapePdfText(final String value) {
        return value.replace("\\", "\\\\").replace("(", "\\(").replace(")", "\\)");
    }

    private String formatMoney(final BigDecimal amount) {
        return amount == null ? "0.00" : amount.toPlainString();
    }

    private byte[] ascii(final String value) {
        return value.getBytes(StandardCharsets.US_ASCII);
    }

    private void writeAscii(final ByteArrayOutputStream output, final String value) {
        output.writeBytes(ascii(value));
    }
}
