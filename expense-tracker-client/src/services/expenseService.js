import axios from "axios"

const BASE_URL = 'http://localhost:1200/expenses'

const expenseService = {
  getExpenses: () => {
    return axios.get(BASE_URL)
  },
  deleteExpense: (id) => {
    return axios.delete(BASE_URL + `/${id}`)
  },
  createExpense: (expense) => {
    return axios.post(BASE_URL, expense)
  },
  updateExpense: (expense) => {
    return axios.put(BASE_URL, expense)
  }
}

export default expenseService