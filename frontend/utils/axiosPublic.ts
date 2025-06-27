import axios, { AxiosResponse } from "axios";
import type { ApiResponse } from "../types/api.response";

const axiosPublic = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_URL,
  timeout: 5000,
  headers: {
    "Content-Type": "application/json",
  },
  withCredentials: true,
});

export const apiPublicCall = async <T, F>(
  url: string,
  method: "GET" | "POST" | "PUT" | "DELETE",
  data?: F
): Promise<ApiResponse<T | null>> => {
  try {
    const { data: rawResponse } = await axiosPublic({
      url,
      method,
      ...(method === "GET" ? { params: data } : { data }),
    });
    const response: ApiResponse<T> = rawResponse;

    return response as ApiResponse<T>;
  } catch (error) {
    if (axios.isAxiosError(error)) {
      const statusCode = error.response?.status || 500;
      const apiErrorCode = error.response?.data?.code || statusCode;
      const apiErrorMessage =
        error.response?.data?.message ||
        "An unexpected error occurred: " + error.message;

      return {
        code: apiErrorCode,
        message: apiErrorMessage,
        data: null,
      };
    }

    return {
      code: 500,
      message: "An unexpected error occurred",
      data: null,
    };
  }
};

export default axiosPublic;
