import { API_HOST } from "$lib/consts"
import axios from "axios"

export const api = axios.create({
	baseURL: API_HOST,
	responseType: "json",
})
