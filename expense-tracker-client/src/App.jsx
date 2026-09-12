import Header from "./components/Header";
import ExpenseForm from "./components/ExpenseForm";
import Summary from "./components/Summary";
import ExpenseList from "./components/ExpenseList";
import Footer from "./components/Footer";
import { useEffect, useState } from "react";
import axios from "axios";

export default function App() {
  const [expenses, setExpenses] = useState([])

  const getExpenses = async () => {
    try {
      const response = await axios.get("http://localhost:1200/expenses")
      setExpenses(response.data)
    } catch(err) {
      console.log("Some Error occurred:-", err)
    }
  }

  useEffect(() => {
    getExpenses()
  }, [])

  return (
    <div className="min-h-screen bg-gray-100">
      <Header />

      <main className="max-w-4xl mx-auto py-4 mt-4">
        <ExpenseForm />
        <Summary expenses={expenses} />
        <ExpenseList expenses={expenses} />
      </main>

      {/* Footer */}
      <Footer />
    </div>
  )
}