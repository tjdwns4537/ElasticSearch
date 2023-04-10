package com.example.elasticsearch.helper;

public enum StockEnum {

    KAKAO("035720","카카오"),
    NAVER("035420","네이버"),
    KIA("000270","기아"),
    SKENOVATION("096770", "SK이노베이션"),
    LGCHEMISTRY("051910","LG화학"),
    SAMSUNG("005930","삼성");

    String number;
    String name;

    StockEnum(String number, String name) {
        this.number = number;
        this.name = name;
    }

    public String getNumber() {
        return this.number;
    }

    public String getName() {
        return this.name;
    }
}
