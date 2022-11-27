package it.corradolombardi.fabanking.fabrikclient;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FabrikPersonBeneficiary {

    private final String fiscalCode;
    private final String fiscalCode1;
    private final String fiscalCode2;
    private final String fiscalCode3;
    private final String fiscalCode4;
    private final String fiscalCode5;
    private final String legalRepresentativeFiscalCode;
}
