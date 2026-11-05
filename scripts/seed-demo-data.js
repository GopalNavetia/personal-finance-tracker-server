const targetEmail = process.env.DEMO_SEED_EMAIL;

if (!targetEmail) {
  throw new Error('Set DEMO_SEED_EMAIL before running this script.');
}

const user = db.users.findOne({ email: targetEmail });

if (!user) {
  throw new Error(`No user found for email: ${targetEmail}`);
}

const userId = user._id.toString();
const existingTransactions = db.transactions.countDocuments({ userId });
const existingCategories = db.categories.countDocuments({ userId });

db.transactions.deleteMany({ userId });
db.categories.deleteMany({ userId });

const expenseCategories = [
  ['Food', 5000],
  ['Rent', 12000],
  ['Transport', 3000],
  ['Shopping', 6000],
  ['Entertainment', 2500],
  ['Health', 4000],
];

const incomeCategories = [
  ['Salary', 0],
  ['Freelance', 0],
  ['Business', 0],
  ['Investment', 0],
];

const categoryDocuments = [...expenseCategories, ...incomeCategories].map(([name, budget]) => ({
  userId,
  name,
  budget,
  _class: 'finance_tracker.model.Category',
}));

const insertedCategories = db.categories.insertMany(categoryDocuments);
const categories = {};

categoryDocuments.forEach((category, index) => {
  categories[category.name] = insertedCategories.insertedIds[index].toString();
});

const today = new Date();
const currentMonth = today.getUTCMonth();
const currentYear = today.getUTCFullYear();
const transactions = [];

function monthDate(monthOffset, requestedDay) {
  const monthStart = new Date(Date.UTC(currentYear, currentMonth - monthOffset, 1));
  const year = monthStart.getUTCFullYear();
  const month = monthStart.getUTCMonth();
  const lastDay = monthOffset === 0
    ? today.getUTCDate()
    : new Date(Date.UTC(year, month + 1, 0)).getUTCDate();
  const day = Math.min(requestedDay, lastDay);
  return new Date(Date.UTC(year, month, day, 18, 30, 0));
}

function addIncome(monthOffset, source, amount, day, description) {
  transactions.push({
    userId,
    type: 'INCOME',
    amount,
    description: `${description} - ${source}`,
    date: monthDate(monthOffset, day),
    _class: 'finance_tracker.model.Transaction',
  });
}

function addExpense(monthOffset, category, amount, day, description) {
  transactions.push({
    userId,
    type: 'EXPENSE',
    amount,
    categoryId: categories[category],
    description,
    date: monthDate(monthOffset, day),
    _class: 'finance_tracker.model.Transaction',
  });
}

for (let monthOffset = 5; monthOffset >= 0; monthOffset -= 1) {
  addIncome(monthOffset, 'Salary', 50000 + (monthOffset * 1500), 2, 'Monthly salary');
  addExpense(monthOffset, 'Rent', 10000, 7, 'Monthly rent');

  if (monthOffset % 2 === 0) {
    addExpense(monthOffset, 'Food', 2400 + (monthOffset * 100), 14, 'Monthly spending');
    addIncome(monthOffset, 'Freelance', 9000 + (monthOffset * 800), 18, 'Freelance project');
    addExpense(monthOffset, 'Shopping', 3200 + (monthOffset * 150), 23, 'Household shopping');
  } else {
    addExpense(monthOffset, 'Transport', 1500 + (monthOffset * 75), 14, 'Monthly transport');
    addExpense(monthOffset, 'Health', 1800 + (monthOffset * 120), 21, 'Health and wellness');
  }
}

db.transactions.insertMany(transactions);

printjson({
  user: targetEmail,
  deletedTransactions: existingTransactions,
  deletedCategories: existingCategories,
  createdCategories: categoryDocuments.length,
  createdTransactions: transactions.length,
  months: [...new Set(transactions.map((transaction) => transaction.date.toISOString().slice(0, 7)))],
});
