package kr.iam.domain.Advertisement.domain;

import jakarta.persistence.*;
import kr.iam.domain.Channel.domain.Channel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Advertisement {
    @Id @GeneratedValue
    @Column(name = "advertisement_id")
    private long id;
    private String url;
    private Double price;

    @OneToMany(mappedBy = "advertisement")
    private List<ChannelAdvertisement> channelAdvertisements = new ArrayList<>();

}
