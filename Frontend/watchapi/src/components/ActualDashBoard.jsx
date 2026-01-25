import React, { useState, useEffect } from 'react';
import Header from './Header';
import StatsGrid from './StatsGrid';
import TabNavigation from './TabNavigation';
import TabContent from './TabContent';
import { fetchDashboardData } from './services/Api.jsx';

export default function EndpointDashboard() {
  const [activeTab, setActiveTab] = useState('overview');
  const [data, setData] = useState({
    endpoints: [],
    metrics: {},
    unused: [],
    recent: []
  });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      setLoading(true);
      const dashboardData = await fetchDashboardData();
      setData(dashboardData);
    } catch (error) {
      console.error('Error fetching data:', error);
    } finally {
      setLoading(false);
    }
  };

  const stats = {
    totalRequests: data.endpoints.reduce((sum, ep) => sum + (ep.metrics?.total || 0), 0),
    activeEndpoints: data.endpoints.filter(ep => (ep.metrics?.total || 0) > 0).length,
    totalEndpoints: data.endpoints.length,
    avgResponseTime: data.endpoints.length > 0
      ? data.endpoints.reduce((sum, ep) => sum + (ep.metrics?.avgResponseMs || 0), 0) / data.endpoints.length
      : 0,
    unusedCount: data.unused.length
  };

  return (
    <div style={{
      minHeight: '100vh',
      backgroundColor: '#000',
      color: '#0f0',
      fontFamily: 'monospace',
      padding: '20px'
    }}>
      <div style={{ maxWidth: '1400px', margin: '0 auto' }}>
        <Header onRefresh={loadData} loading={loading} />
        <StatsGrid stats={stats} />
        <TabNavigation activeTab={activeTab} onTabChange={setActiveTab} />
        <TabContent 
          activeTab={activeTab}
          data={data}
          loading={loading}
        />
      </div>
    </div>
  );
}