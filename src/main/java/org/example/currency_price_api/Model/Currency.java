package org.example.currency_price_api.Model;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

/*
*建立幣別以及中文對應名稱
CREATE TABLE Currency (
    id LONG AUTO_INCREMENT PRIMARY KEY ,
    code varchar(100),
    chineseName varchar(100)
)
*/
@Entity
@Getter
@Setter
public class Currency {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //幣別代碼
    @Column(name = "CODE")
    private String code;

    //幣別對應的中文名稱
    @Column(name = "CHINESENAME")
    private String chineseName;
}
