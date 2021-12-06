package org.egc.commons.gis;

import java.util.Arrays;
import java.util.LinkedList;

public class Histogram {
    public LinkedList<Bucket> buckets;
    private double[] quantiles;
    public long avgItemsPerQuantile;


    /**
    * 构建直方图的数据结构
    *
    * @param numQuantiles 预期的分位数数量，比如2个分位数可以把数据分为3组
    * @param buckets 每个桶的栅格数量
    * @param min 栅格图层的最小值
    * @param max 栅格图层的最大值
     */
    public Histogram(int numQuantiles, double min, double max, long[] buckets){
        this.buckets=new LinkedList<>();
        this.quantiles=new double[numQuantiles];
        long nonemptySize= Arrays.stream(buckets).sum();
        this.avgItemsPerQuantile=nonemptySize/(numQuantiles+1);

        int bucketNum = buckets.length;
        double interval=(max-min)/bucketNum;
        for (int i = 0; i < bucketNum; i++) {
            double bMin=min+i*interval;
            double bMax=min+(i+1)*interval;
            Bucket b = new Bucket(bMin,bMax,buckets[i]);
            this.buckets.add(b);
        }

        //init approximate quantiles
        for (int i = 0; i < this.quantiles.length; i++) {
            double q=nextQuantile(avgItemsPerQuantile);
            if(q==Double.MIN_VALUE){
                break;
            }
            this.quantiles[i]=q;
        }
    }

    public double[] getApproximateQuantiles(){
        return quantiles;
    }

    private double nextQuantile(long items){
        Bucket bn=this.buckets.peek();
        if(bn==null){
            return Double.MIN_VALUE;
        }
        double q=0;
        // 之前抽过的，加上当前这桶就能够满足需求，就取所需的
        if(bn.remain>=items){
            q=bn.drain(items);
            if(bn.remain==0){
                this.buckets.poll();
            }
            return q;
        }
        //如果之前抽过的加上当前这桶不能满足需求
        bn=this.buckets.poll();
        q = nextQuantile(items-bn.remain);
        if(q==Double.MIN_VALUE){//这时候应该是取完了所有桶，但分位数算法出了问题
            return bn.drain(bn.remain);
        }
        return q;
    }
}