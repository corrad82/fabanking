package it.corradolombardi.fabanking.fabrikclient;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FabrikTaxRelief {

    private final String taxReliefId;
    private final boolean isCondoUpgrade;
    private final String creditorFiscalCode;
    private final String beneficiaryType;
    private final FabrikPersonBeneficiary naturalPersonBeneficiary;
    private final FabrikPersonBeneficiary legalPersonBeneficiary;

}
