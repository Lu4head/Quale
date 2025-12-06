package br.com.quale.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.TypeAlias;

import java.util.List;

@TypeAlias("GROUP")
@Data
@EqualsAndHashCode(callSuper = true)
public class GroupChat extends Chat {
    private String groupName;
    private String groupDescription;
    private String groupImageUrl;
    private List<Long> adminIds;
}