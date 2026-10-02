import axios from 'axios'
import React, { useEffect, useState } from 'react'
import expenseService from '../services/expenseService'

const ExpenseForm = ({ getExpenses, editingExpense, setEditingExpense }) => {
  const [title, setTitle] = useState('')
  const [category, setCategory] = useState('')
  const [price, setPrice] = useState('')
  const [date, setDate] = useState('')
  const [errors, setErrors] = useState({})

  useEffect(() => {
    if (editingExpense) {
      setErrors({})

      setTitle(editingExpense.title)
      setCategory(editingExpense.category)
      setPrice(editingExpense.price)
      setDate(editingExpense.date)

      window.scrollTo({top:0 , behavior: 'smooth'})
    }
  }, [editingExpense])

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!validate()) return

    const expense = {
      id: editingExpense ? editingExpense.id : 0,
      title,
      price,
      category,
      date
    }

    if(editingExpense) {
      await updateExpense(expense)
    } else {
      await createExpense(expense)
    }
  }

  async function updateExpense(expense) {
    try {
      const response = await expenseService.updateExpense(expense)
  
      if (response.status === 202) {
        getExpenses()
        clearForm()
        setEditingExpense(null)
      } else {
        alert("Something went wrong !!!")
      }
    } catch (err) {
      console.log("Some Error occurred:-", err)
    }
  }

  async function createExpense(expense) {
    try {
      const response = await expenseService.createExpense(expense)

      if (response.status === 201) {
        getExpenses()
        clearForm()
      } else {
        alert("Something went wrong !!!")
      }
    } catch (err) {
      console.log("Some Error occurred:-", err)
    }
  }

  const validate = () => {
    const newErros = {}

    if (!title) {
      newErros.title = 'Title is missing.'
    } else if (title.length <= 3) {
      newErros.title = 'Title must have atlease 3 characters.'
    }

    if (!category) {
      newErros.category = 'Please choose a valid category.'
    }

    if (!price || isNaN(price) || price <= 0) {
      newErros.price = 'Price must be greater than 0.'
    }

    if (!date) {
      newErros.date = 'Date is required.'
    }

    setErrors(newErros)
    return Object.keys(newErros).length === 0 // {} -> true, {title: ''} -> false
  }

  const clearForm = () => {
    setTitle('')
    setCategory('')
    setPrice('')
    setDate('')
  }

  const handleChange = (e) => {
    const { name, value } = e.target

    switch (name) {
      case 'title':
        setTitle(value)
        setErrors(prev => ({ ...prev, title: '' }))
        break;
      case 'category':
        setCategory(value)
        setErrors(prev => ({ ...prev, category: '' }))
        break;
      case 'price':
        setPrice(value)
        setErrors(prev => ({ ...prev, price: '' }))
        break;
      case 'date':
        setDate(value)
        setErrors(prev => ({ ...prev, date: '' }))
        break;
    }
  }

  const handleCancel = () => {
    setEditingExpense(null)
    clearForm()
  }

  return (
    <div className='bg-white rounded-2xl shadow-md p-6 mb-6'>
      <h2 className='text-xl font-semibold text-gray-700 mb-4'>
        {editingExpense ? 'Edit' : 'Add'} Expense
      </h2>

      <form action={"www.ggogle.com"} onSubmit={handleSubmit} className='grid grid-cols-1 md:grid-cols-2 gap-4'>
        {/* Title */}
        <div>
          <label htmlFor="" className='block font-medium text-gray-600 mb-1'>Title</label>
          <input placeholder='e.g; House Rent' type="text" className='border w-full border-gray-300 rounded-lg px-3 py-2 focus:outline-none focus:border-blue-500' onChange={handleChange} name='title' value={title} />

          {
            errors.title && (
              <p className='text-red-500 mt-1 text-sm'>{errors.title}</p>
            )
          }
        </div>

        {/* Category */}
        <div>
          <label htmlFor="" className='block font-medium text-gray-600 mb-1'>Category</label>
          <select id="" className='border w-full border-gray-300 rounded-lg px-3 py-2 focus:outline-none focus:border-blue-500' onChange={handleChange} name='category' value={category}>
            <option value="" >-- Select Category --</option>
            <option value="food" >Food</option>
            <option value="travel" >Travel</option>
            <option value="utilities" >Utilities</option>
            <option value="shopping" >Shopping</option>
            <option value="entertainment" >Entertainment</option>
            <option value="health" >Health</option>
            <option value="education" >Education</option>
            <option value="others" >Others</option>
          </select>

          {
            errors.category && (
              <p className='text-red-500 mt-1 text-sm'>{errors.category}</p>
            )
          }
        </div>

        {/* Price */}
        <div>
          <label htmlFor="" className='block font-medium text-gray-600 mb-1'>Price</label>
          <input placeholder='e.g; 5000.99' type="text" className='border w-full border-gray-300 rounded-lg px-3 py-2 focus:outline-none focus:border-blue-500' onChange={handleChange} name='price' value={price} />

          {
            errors.price && (
              <p className='text-red-500 mt-1 text-sm'>{errors.price}</p>
            )
          }
        </div>

        {/* Date */}
        <div>
          <label htmlFor="" className='block font-medium text-gray-600 mb-1'>Date</label>
          <input type="date" className='border w-full border-gray-300 rounded-lg px-3 py-2 focus:outline-none focus:border-blue-500' onChange={handleChange} name='date' value={date} />

          {
            errors.date && (
              <p className='text-red-500 mt-1 text-sm'>{errors.date}</p>
            )
          }
        </div>

        {/* Add Expense Button */}
        <div className='mt-5'>
          {
            editingExpense ?
              <div className='flex gap-2'>
                <button className='bg-green-500 hover:bg-green-600 px-6 py-3 rounded-lg text-white text-lg font-medium transition-colors duration-200' >
                  Update Expense
                </button>
                <button onClick={handleCancel} className='bg-yellow-500 hover:bg-yellow-600 px-6 py-3 rounded-lg text-white text-lg font-medium transition-colors duration-200' >
                  Cancel
                </button>
              </div> :
              <button className='bg-green-500 hover:bg-green-600 px-6 py-3 rounded-lg text-white text-lg font-medium transition-colors duration-200' >
                Add Expense
              </button>
          }
        </div>
      </form>
    </div>
  )
}

export default ExpenseForm