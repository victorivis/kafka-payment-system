package ifpb.edu.br.consumer.repository;

import ifpb.edu.br.consumer.entity.PaymentMethod;
import ifpb.edu.br.consumer.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
