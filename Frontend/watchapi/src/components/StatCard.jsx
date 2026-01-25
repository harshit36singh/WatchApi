import React from 'react';

export default function StatCard({ title, value, color = '#0f0' }) {
  return (
    <div style={{
      backgroundColor: '#0a0a0a',
      border: `1px solid ${color}`,
      padding: '15px'
    }}>
      <div style={{ fontSize: '11px', color: '#0a0', marginBottom: '5px' }}>
        {title}
      </div>
      <div style={{ fontSize: '24px', color, fontWeight: 'bold' }}>
        {value}
      </div>
    </div>
  );
}