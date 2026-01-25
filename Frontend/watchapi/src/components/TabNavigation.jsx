import React from 'react';

const TABS = ['overview', 'metrics', 'unused', 'recent'];

export default function TabNavigation({ activeTab, onTabChange }) {
  return (
    <div style={{ display: 'flex', gap: '2px', marginBottom: '20px' }}>
      {TABS.map(tab => (
        <button
          key={tab}
          onClick={() => onTabChange(tab)}
          style={{
            padding: '10px 20px',
            backgroundColor: activeTab === tab ? '#0f0' : '#111',
            color: activeTab === tab ? '#000' : '#0f0',
            border: '1px solid #0f0',
            cursor: 'pointer',
            fontFamily: 'monospace',
            fontSize: '12px',
            textTransform: 'uppercase',
            fontWeight: activeTab === tab ? 'bold' : 'normal'
          }}
        >
          {tab}
        </button>
      ))}
    </div>
  );
}