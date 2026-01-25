import React from 'react';
import OverviewTab from './tabs/OverviewTab';
import MetricsTab from './tabs/MetricsTab';
import UnusedTab from './tabs/UnusedTab';
import RecentTab from './tabs/RecentTab';

export default function TabContent({ activeTab, data, loading }) {
  return (
    <div style={{
      backgroundColor: '#0a0a0a',
      border: '1px solid #0f0',
      padding: '20px'
    }}>
      {loading ? (
        <div style={{ textAlign: 'center', padding: '40px' }}>
          LOADING...
        </div>
      ) : (
        <>
          {activeTab === 'overview' && (
            <OverviewTab 
              endpoints={data.endpoints} 
              metrics={data.metrics} 
            />
          )}
          {activeTab === 'metrics' && (
            <MetricsTab endpoints={data.endpoints} />
          )}
          {activeTab === 'unused' && (
            <UnusedTab unused={data.unused} />
          )}
          {activeTab === 'recent' && (
            <RecentTab recent={data.recent} />
          )}
        </>
      )}
    </div>
  );
}