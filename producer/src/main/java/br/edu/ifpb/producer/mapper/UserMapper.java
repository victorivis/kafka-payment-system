package br.edu.ifpb.producer.mapper;

import br.edu.ifpb.producer.dto.UserRequest;
import br.edu.ifpb.producer.dto.UserResponse;
import br.edu.ifpb.producer.entity.PermissionEntity;
import br.edu.ifpb.producer.entity.UserEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class UserMapper {

    public static UserEntity toEntity(UserRequest request) {
        UserEntity entity = new UserEntity();
        entity.setName(request.getName());
        entity.setEmail(request.getEmail());
        return entity;
    }

    public static UserResponse toResponse(UserEntity entity) {
        return UserResponse.builder()
                .name(entity.getName())
                .email(entity.getEmail())
                .permissions(buildPermissionsResponse(entity.getPermissions()))
                .build();
    }

    private static List<String> buildPermissionsResponse(List<PermissionEntity> permissions) {
        return permissions.stream()
                .map(PermissionEntity::getName)
                .toList();
    }
}
