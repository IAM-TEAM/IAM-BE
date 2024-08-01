package kr.iam.domain.using_category.application;

import kr.iam.domain.channel.domain.Channel;
import kr.iam.domain.using_category.dao.UsingCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsingCategoryService {
    private final UsingCategoryRepository usingCategoryRepository;

    public void deleteAllByChannel(Channel channel) {
        usingCategoryRepository.deleteAllByChannel(channel);
    }
}
