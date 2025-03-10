import request from "@/api/utils/request";
import axios from "axios";


//接口
interface MallProduct{
    id: bigint,
    name:string,
    description:string,
    price:number,
    stock:number,
    categoryName:string,
    covor:string,
    post_time,
    update_time,
    status:number,
}
//订单表接口
interface MallOrder{
    id: bigint,
    userId:string,
    username:string,
    productId:bigint,
    productName:string,
    phone:string,
    address:string,
    stock:number,
    price:number,
    post_time:Date,
    update_time:Date,
    status:number
}