package org.eclipse.tractusx.traceability.assets.infrastructure.base.irs.model.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import org.eclipse.tractusx.traceability.generated.Batch300Schema;
import org.eclipse.tractusx.traceability.generated.JustInSequencePart300Schema;
import org.eclipse.tractusx.traceability.generated.PartAsPlanned200Schema;
import org.eclipse.tractusx.traceability.generated.PartSiteInformationAsPlanned100Schema;
import org.eclipse.tractusx.traceability.generated.SerialPart300Schema;
import org.eclipse.tractusx.traceability.generated.TractionBatteryCode100Schema;

@Getter
public class IrsSubmodel {
    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
            defaultImpl = Void.class,
            property = "aspectType")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = SerialPart300Schema.class, names = {
                    "urn:samm:io.catenax.serial_part:3.0.0#SerialPart",
            }),
            @JsonSubTypes.Type(value = Batch300Schema.class, names = {
                    "urn:samm:io.catenax.batch:3.0.0#Batch"
            }),
            @JsonSubTypes.Type(value = PartAsPlanned200Schema.class, names = {
                    "urn:samm:io.catenax.part_as_planned:2.0.0#PartAsPlanned",
            }),
            @JsonSubTypes.Type(value = PartSiteInformationAsPlanned100Schema.class, names = {
                    "urn:samm:io.catenax.part_site_information_as_planned:1.0.0#PartSiteInformationAsPlanned"
            }),
            @JsonSubTypes.Type(value = JustInSequencePart300Schema.class, names = {
                    "urn:samm:io.catenax.just_in_sequence_part:3.0.0#JustInSequencePart"
            }),
            @JsonSubTypes.Type(value = TractionBatteryCode100Schema.class, names = {
                    "urn:samm:io.catenax.traction_battery_code:1.0.0#TractionBatteryCode"
            })
    })
    private Object payload;

    @JsonProperty("aspectType")
    private String aspectType;

    @JsonProperty("identification")
    private String identification;

    @JsonCreator
    public IrsSubmodel(@JsonProperty("aspectType") String aspectType, @JsonProperty("payload") Object payload) {
        this.aspectType = aspectType;
        this.payload = payload;
    }

}
