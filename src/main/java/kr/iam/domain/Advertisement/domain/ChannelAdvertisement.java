package kr.iam.domain.Advertisement.domain;

import jakarta.persistence.*;
import kr.iam.domain.Channel.domain.Channel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChannelAdvertisement {
    @Id @GeneratedValue
    @Column(name = "channel_advertisement_id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "channel_id")
    private Channel channel;

    @ManyToOne
    @JoinColumn(name = "advertisement_id")
    private Advertisement advertisement;
}
