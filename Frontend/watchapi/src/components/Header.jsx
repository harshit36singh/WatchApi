import React from 'react';

export default function Header({ onRefresh, loading }) {
  return (
    <div style={{ 
      marginBottom: '30px', 
      borderBottom: '1px solid #0f0', 
      paddingBottom: '15px',
      display: 'flex',
      justifyContent: 'space-between',
      alignItems: 'center'
    }}>
      <div>
        <h1 style={{ margin: 0, fontSize: '24px', color: '#0f0' }}>
          WatchApi
        </h1>
        <p style={{ margin: '5px 0 0 0', fontSize: '12px', color: '#0a0' }}>
          Live monitoring dashboard
        </p>
      </div>
      <button
        onClick={onRefresh}
        disabled={loading}
        style={{
          padding: '10px 20px',
          backgroundColor: loading ? '#333' : '#0f0',
          color: loading ? '#666' : '#000',
          border: '1px solid #0f0',
          cursor: loading ? 'not-allowed' : 'pointer',
          fontFamily: 'monospace',
          fontSize: '12px',
          textTransform: 'uppercase',
          fontWeight: 'bold'
        }}
      >
        {loading ? 'REFRESHING...' : '↻ REFRESH'}
      </button>
    </div>
  );
}
