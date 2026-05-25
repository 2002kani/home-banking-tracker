import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import AppLayout from "./components/layout/appLayout";
import TransactionsPage from "./pages/transactions/transactionsPage";
import DashboardPage from "./pages/dashboard/dashboardPage";
import CategoriesPage from "./pages/categories/categoriesPage";
import AccountsPage from "./pages/accounts/accountsPage";
import AnalyticsPage from "./pages/analytics/analyticsPage";
import SettingsPage from "./pages/settings/settingsPage";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<AppLayout />}>
          <Route index element={<Navigate to="/dashboard" replace />} />
          <Route path="dashboard" element={<DashboardPage />} />
          <Route path="transactions" element={<TransactionsPage />} />
          <Route path="categories" element={<CategoriesPage />} />
          <Route path="accounts" element={<AccountsPage />} />
          <Route path="analytics" element={<AnalyticsPage />} />
          <Route path="settings" element={<SettingsPage />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;
