import React from 'react';
import StatCard from './StatCard';

export default function StatsGrid({ stats }) {
  return (
    <div style={{
      display: 'grid',
      gridTemplateColumns: 'repeat(auto-fit, minmax(250px, 1fr))',
      gap: '15px',
      marginBottom: '30px'
    }}>
      <StatCard 
        title="TOTAL REQUESTS" 
        value={stats.totalRequests.toLocaleString()} 
      />
      <StatCard 
        title="ACTIVE ENDPOINTS" 
        value={`${stats.activeEndpoints}/${stats.totalEndpoints}`} 
      />
      <StatCard 
        title="AVG RESPONSE" 
        value={`${stats.avgResponseTime.toFixed(2)}ms`} 
      />
      <StatCard 
        title="UNUSED" 
        value={stats.unusedCount} 
        color="#f00" 
      />
    </div>
  );
}