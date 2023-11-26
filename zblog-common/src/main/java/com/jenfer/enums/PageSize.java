package com.jenfer.enums;

public enum PageSize {
    SIZE5(5),SIZE10(10),SIZE15(15),SIZE20(20),SIZE30(30),SIZE40(40),SIZE50(50);
    int size;
    private PageSize(int size){
        this.size = size;
    }
    public int getSize(){
        return this.size;
    }
}
