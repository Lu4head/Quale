package br.com.quale.dto;

import br.com.quale.entity.GroupChat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class GroupChatResponseDTO extends ChatResponseDTO{
    private String groupName;
    private String groupDescription;
    private String groupImageUrl;
    private java.util.List<Long> adminIds;

    public GroupChatResponseDTO(GroupChat groupChat){
        super(groupChat);
        this.groupName = groupChat.getGroupName();
        this.groupDescription = groupChat.getGroupDescription();
        this.groupImageUrl = groupChat.getGroupImageUrl();
        this.adminIds = groupChat.getAdminIds();
    }
}
