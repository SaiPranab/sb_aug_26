import Header from "./components/Header";
import ExpenseForm from "./components/ExpenseForm";
import Summary from "./components/Summary";

export default function App() {
  return (
    <div className="min-h-screen bg-gray-100">
      <Header />

      <main className="max-w-4xl mx-auto py-4 mt-4">
        <ExpenseForm />
        <Summary />
      </main>

      {/* Footer */}
    </div>
  )
}