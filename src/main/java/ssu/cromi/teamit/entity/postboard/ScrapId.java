package ssu.cromi.teamit.entity.postboard;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ScrapId implements Serializable {
    private String user;
    private Long board;
}