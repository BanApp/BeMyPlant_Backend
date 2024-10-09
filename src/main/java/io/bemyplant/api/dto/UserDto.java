package io.bemyplant.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import io.bemyplant.api.entity.UserInfo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @NotNull
    @Size(min = 3, max = 50)
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    @Size(min = 3, max = 100)
    private String password;

    @NotNull
    @Size(min = 3, max = 50)
    private String r_name;

    @NotNull
    @Size(min = 8, max = 20)
    private String phones;

    @NotNull
    @Size(min = 8, max = 30)
    private String cre_date;

    private Set<AuthorityDto> authorityDtoSet;

    public static UserDto from(UserInfo user) {
        if(user == null) return null;

        return UserDto.builder()
                .username(user.getUsername())
                .r_name(user.getR_name())
                .phones(user.getPhones())
                .cre_date(user.getCre_date())
                .authorityDtoSet(user.getAuthorities().stream()
                        .map(authority -> AuthorityDto.builder().authorityName(authority.getAuthorityName()).build())
                        .collect(Collectors.toSet()))
                .build();
    }
}