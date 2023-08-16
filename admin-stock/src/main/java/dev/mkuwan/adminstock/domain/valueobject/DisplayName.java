package dev.mkuwan.adminstock.domain.valueobject;

public record DisplayName(String name) {
    /**
     * 販売用商品名は20文字以内
     */
    public DisplayName{
        if(name.length() == 0 || name.length() > 20){
            throw new IllegalArgumentException("販売用商品名は1文字以上、20文字以内にしてください");
        }
    }
}
