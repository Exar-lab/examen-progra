package com.buses.examen.Progra.customer.adapter.out.security;

import com.buses.examen.Progra.customer.application.command.RawCardData;
import com.buses.examen.Progra.customer.application.port.out.CardDataProtectorPort;
import com.buses.examen.Progra.customer.application.result.ProtectedCardData;
import org.springframework.stereotype.Component;

/** Protector temporal que normaliza y enmascara sin persistir CVV. */
@Component
public class PassThroughCardDataProtectorAdapter implements CardDataProtectorPort {
    private static final String PREFIX_TOKEN = "tok-";
    private static final String MASK_MIDDLE = "******";
    private static final String BRAND_VISA = "VISA";
    private static final String BRAND_MASTERCARD = "MASTERCARD";
    private static final String BRAND_UNKNOWN = "UNKNOWN";
    private static final int CARD_VISIBLE_DIGITS = 4;

    /**
     * {@inheritDoc}
     *
     * @param rawCardData datos de tarjeta en crudo
     * @return datos protegidos aptos para persistencia
     */
    @Override
    public ProtectedCardData protect(final RawCardData rawCardData) {
        final String pan = rawCardData.numeroTarjeta().trim();
        final String first4 = pan.substring(0, CARD_VISIBLE_DIGITS);
        final String ultimo4 = pan.substring(pan.length() - CARD_VISIBLE_DIGITS);
        final String brand = detectBrand(pan);
        final String token = PREFIX_TOKEN + ultimo4;
        final String masked = first4 + MASK_MIDDLE + ultimo4;
        return new ProtectedCardData(brand, ultimo4, token, masked);
    }

    private String detectBrand(final String pan) {
        if (pan.startsWith("4")) {
            return BRAND_VISA;
        }
        if (pan.startsWith("5")) {
            return BRAND_MASTERCARD;
        }
        return BRAND_UNKNOWN;
    }
}
