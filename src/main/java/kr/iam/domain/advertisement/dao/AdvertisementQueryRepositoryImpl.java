package kr.iam.domain.advertisement.dao;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.iam.domain.advertisement.dto.info.AdvertisementInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static kr.iam.domain.advertisement.domain.QAdvertisement.advertisement;

@RequiredArgsConstructor
public class AdvertisementQueryRepositoryImpl implements AdvertisementQueryRepository{
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<AdvertisementInfo> advertisementList(Pageable pageable) {
        List<AdvertisementInfo> contents = queryFactory.select(
                Projections.constructor(AdvertisementInfo.class,
                        advertisement.id,
                        advertisement.title,
                        advertisement.url,
                        advertisement.startDate,
                        advertisement.endDate,
                        advertisement.requirement,
                        advertisement.price,
                        advertisement.content
                        ))
                .from(advertisement)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(advertisement.id.countDistinct());
        return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchCount);
    }
}
