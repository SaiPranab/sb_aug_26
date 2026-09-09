import React from 'react'

const Header = () => {
  return (
    <>
      <header className='bg-blue-600 text-white py-6 shadow-md text-center'>
        <div className='px-4'>
          <h1 className='font-bold text-3xl'>Personal Expense Tracker</h1>
          <p className='text-blue-200 text-lg mt-1'>Track your daily expenses with ease</p>
        </div>
      </header>
    </>
  )
}

export default Header