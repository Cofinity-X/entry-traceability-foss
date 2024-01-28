package org.eclipse.tractusx.traceability.integration.submodel;

import org.eclipse.tractusx.traceability.integration.IntegrationTestSpecification;
import org.eclipse.tractusx.traceability.submodel.domain.service.SubmodelServerServiceImpl;
import org.eclipse.tractusx.traceability.submodel.infrastructure.model.SubmodelEntity;
import org.eclipse.tractusx.traceability.submodel.infrastructure.repository.JpaSubmodelRepository;
import org.eclipse.tractusx.traceability.submodel.infrastructure.repository.SubmodelServerClientImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class SubmodelServerClientTest extends IntegrationTestSpecification {

    @Autowired
    SubmodelServerServiceImpl SubmodelServerService;

    @Autowired
    JpaSubmodelRepository repository;

    @Test
    void givenSubmodel_whenSaveSubmodel_thenIsPersistedToServer() {
        // given
        String submodelId = "ID_OF_SUBMODEL";
        String submodel = "SUBMODEL PAYLOAD";

        // when
        String savedId = SubmodelServerService.saveSubmodel(submodelId, submodel);

        // then
        Optional<SubmodelEntity> result = repository.findById(submodelId);
        assertThat(result.get().getSubmodel()).isEqualTo(submodel);
        assertThat(savedId).isEqualTo(submodelId);
    }

    @Test
    void givenSubmodel_whenGetSubmodel_thenIsRetrievedFromServer() {
        // given
        String submodelId = "1234";
        String submodel = "SUBMODEL PAYLOAD";
        repository.save(SubmodelEntity.builder()
                        .id(submodelId)
                        .submodel(submodel)
                .build());

        // when
        String result = SubmodelServerService.getSubmodel(submodelId);

        // then
        assertThat(result).isEqualTo(submodel);

    }

}
