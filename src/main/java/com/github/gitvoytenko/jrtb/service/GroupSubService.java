package com.github.gitvoytenko.jrtb.service;

import com.github.gitvoytenko.jrtb.javarushclient.dto.GroupDiscussionInfo;
import com.github.gitvoytenko.jrtb.repository.entity.GroupSub;

/**
 * Service for manipulating with {@link GroupSub}
 */
public interface GroupSubService {
    GroupSub save(Long chatId, GroupDiscussionInfo groupDiscussionInfo);
}
