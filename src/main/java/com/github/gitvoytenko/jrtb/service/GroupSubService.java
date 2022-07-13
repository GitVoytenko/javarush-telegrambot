package com.github.gitvoytenko.jrtb.service;

import com.github.gitvoytenko.jrtb.javarushclient.dto.GroupDiscussionInfo;
import com.github.gitvoytenko.jrtb.repository.entity.GroupSub;

import java.util.List;
import java.util.Optional;

/**
 * Service for manipulating with {@link GroupSub}
 */
public interface GroupSubService {
    GroupSub save(Long chatId, GroupDiscussionInfo groupDiscussionInfo);

    GroupSub save(GroupSub groupSub);

    Optional<GroupSub> findById(Integer id);
}
