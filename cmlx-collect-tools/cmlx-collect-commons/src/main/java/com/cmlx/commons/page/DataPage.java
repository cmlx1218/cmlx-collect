package com.cmlx.commons.page;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 18:05
 * @Desc -> APP分页包装类
 **/
@Data
@NoArgsConstructor
public class DataPage<T> implements Serializable {
    private Integer hasNext;         //是否还有下一页
    private Long lastTime;          //下一页请求时间戳
    private Long lastId;            //上一页请求的最后一条数据ID
    private Double lastScore;         //上一页最后一条分数 //FIXME CMLX:2019-10-24 上一页最后一条分数
    private List<T> list;           //内容
    private Integer totalPage;      //总页数
    private Integer totalCount;     //数据总条数
    private Integer errorAmount;  //之前不符合条件的热门用户数量

    public static <T> DataPage init(List<T> list, Integer hasNext, Long lastTime, Integer totalPage) {
        DataPage<T> page = new DataPage();
        page.setList(list);
        page.setHasNext(hasNext);
        page.setLastTime(lastTime);
        page.setTotalPage(totalPage);
        return page;
    }


    public DataPage(Integer hasNext, Long lastTime, Long lastId, Double lastScore, List<T> list, Integer totalPage, Integer totalCount, Integer errorAmount) {
        this.hasNext = hasNext;
        this.lastTime = lastTime;
        this.lastId = lastId;
        this.list = list;
        this.totalPage = totalPage;
        this.totalCount = totalCount;
        this.lastScore = lastScore;
        this.errorAmount = errorAmount;
    }

    public static <T> DataPage.Builder custom() {
        return new Builder();
    }

    @Accessors(chain = true)
    public static class Builder<T> {
        private Integer hasNext;         //是否还有下一页
        private Long lastTime;          //下一页请求时间戳
        private Long lastId;            //上一页请求的最后一条数据ID
        private Double lastScore;       //上一页最后一天数据的分数
        private List<T> list;           //内容
        private Integer totalPage;      //总页数
        private Integer totalCount;     //数据总条数
        private Integer errorAmount;  //之前不符合条件的热门用户数量


        public Builder hasNext(Integer hasNext) {
            this.hasNext = hasNext;
            return this;
        }

        public Builder lastTime(Long lastTime) {
            this.lastTime = lastTime;
            return this;
        }

        public Builder lastId(Long lastId) {
            this.lastId = lastId;
            return this;
        }

        public Builder lastScore(Double lastScore) {
            this.lastScore = lastScore;
            return this;
        }

        public Builder<T> list(List<T> list) {
            this.list = list;
            return this;
        }

        public Builder totalPage(Integer totalPage) {
            this.totalPage = totalPage;
            return this;
        }

        public Builder totalCount(Integer totalCount) {
            this.totalCount = totalCount;
            return this;
        }

        public Builder errorAmount(Integer errorAmount) {
            this.errorAmount = errorAmount;
            return this;
        }

        public DataPage build() {
            return new DataPage(hasNext, lastTime, lastId, lastScore, list, totalPage, totalCount, errorAmount);
        }
    }

}

