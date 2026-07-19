package br.edu.ifpb.producer.repository;

import br.edu.ifpb.producer.entity.PaymentEntity;
import br.edu.ifpb.producer.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<PaymentEntity, UUID> {
    List<PaymentEntity> findByUser(UserEntity user);
    boolean existsByIdAndUser(UUID paymentId, UserEntity user);
}
