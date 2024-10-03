package kr.iam.domain.category.helper;

import kr.iam.domain.category.dao.UsingCategoryRepository;
import kr.iam.domain.channel.domain.Channel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UsingCategoryHelper {
    private final UsingCategoryRepository usingCategoryRepository;

    @Transactional
    public void deleteAllByChannel(Channel channel) {
        usingCategoryRepository.deleteAllByChannel(channel);
    }
}
