package kr.iam.domain.advertisement.domain;

import jakarta.persistence.*;
import kr.iam.domain.BaseTimeEntity;
import kr.iam.domain.channel_advertisement.domain.ChannelAdvertisement;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Advertisement extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "advertisement_id")
    private long id;
    private String url;
    private Double price;

    @OneToMany(mappedBy = "advertisement")
    private List<ChannelAdvertisement> channelAdvertisements = new ArrayList<>();

}
