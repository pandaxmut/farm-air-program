// 使用axios发送请求
import axios from "axios";
import request from "@/api/utils/request";


// 用户实体类
export interface User {
  userId: string;
  username: string;
  feishuId: string;
  description: string;
  avatarUrl: string;
  age: number;
  sex: number;
  email: string;
  phone: string;
  role: number;
  status: number;
  totalIntegral: number;
  currentIntegral: number;
  createTime: string;
  updateTime: string;
}


// 定义一个函数来发送 POST 请求
// export async function getUser(id: number): Promise<User> {
//     try {
//       const response = await axios.get(`/api/users/${id}`);
//       console.log('User created successfully:', response.data);
//       return response.data;
//     } catch (error) {
//       console.error('Error creating user:', error);
//       throw error;
//     }
//   }

// 获取当前用户信息
export async function getUser() {
  try {
    const response = await request.get(`/api/users/users`, {
      
    });
    console.log('User fetched successfully:', response.data);
    console.log('User fetched successfully:', response.data.data);

    return response.data; 
  } catch (error) {
    console.error('Error fetching user:', error);
    throw error;
  }
}

//更新当前用户信息
export async function updateUser(user: User) {
  try {
    const response = await request.put(`/api/users/users`, user, {

    });
    console.log('User updated successfully:', response.data);
    return response.data;
  } catch (error) {
    console.error('Error updating user:', error);
    throw error;
  }
}
//修改用户密码
export async function updateUserPassword(id:string, password:string,newPassword:string) {
  try {
    const response = await request.put(`/api/users/users/password`, {
      "id": id,
      "password": password,
      "newPassword": newPassword
    }, {

    });
    console.log('User updated successfully:', response.data);
    return response.data;
  } catch (error) {
    console.error('Error updating user:', error);
    throw error;
  }
}
//修改角色信息
export async function updateUserRole(password:string, role:number) {
  try {
    const response = await request.put(`/api/users/users/role`, {
      "password":password,
      "role":role
    })
    console.log('User updated successfully:', response.data);
    return response.data;
  }catch (error){
    console.error('Error updating user:', error);
    throw error;
  }
}