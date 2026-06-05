/* ============================================================
   UEP SORS — Shared JS
   Handles guest mode, user state, and reaction gating
   ============================================================ */

// ── User State ──
function getUser() {
  return localStorage.getItem('sors_user') || 'guest';
}

function isGuest() {
  return getUser() === 'guest';
}

function isStudent() {
  return getUser() === 'student';
}

// ── Guest Gate ──
// Call this on pages that require login (apphistory, register)
function requireLogin(redirectMsg) {
  if (isGuest()) {
    showGuestModal(redirectMsg || 'You need to be logged in to access this page.');
    return true;
  }
  return false;
}

// ── Guest Modal ──
function showGuestModal(message) {
  // Remove existing modal if any
  const existing = document.getElementById('guest-modal');
  if (existing) existing.remove();

  const modal = document.createElement('div');
  modal.id = 'guest-modal';
  modal.style.cssText = `
    position: fixed; inset: 0;
    background: rgba(19,16,69,0.75);
    display: flex; justify-content: center; align-items: center;
    z-index: 9999; padding: 20px;
  `;

  modal.innerHTML = `
    <div style="
      background: #fff; border-radius: 20px; padding: 40px 36px;
      max-width: 400px; width: 100%; text-align: center;
      box-shadow: 0 24px 64px rgba(0,0,0,0.3);
    ">
      <div style="font-size: 40px; margin-bottom: 16px;">🔒</div>
      <h2 style="font-family:'Playfair Display',serif; font-size:22px; color:#131045; margin-bottom:10px;">Login Required</h2>
      <p style="font-size:14px; color:#666; line-height:1.7; margin-bottom:28px;">${message}</p>
      <div style="display:flex; gap:12px; justify-content:center;">
        <button onclick="window.location.href='org.html'" style="
          font-family:'Poppins',sans-serif; font-size:14px; font-weight:600;
          color:#131045; background:#f7f7f9; border:2px solid #e2e2e8;
          border-radius:50px; padding:11px 24px; cursor:pointer;
        ">Stay as Guest</button>
        <a href="login.html" style="
          font-family:'Poppins',sans-serif; font-size:14px; font-weight:700;
          color:#131045; background:#FFD700; border:none;
          border-radius:50px; padding:11px 24px; cursor:pointer;
          text-decoration:none; display:inline-block;
        ">Sign In</a>
      </div>
    </div>
  `;

  document.body.appendChild(modal);
}

// ── Reaction Gate ──
// Wraps the react() function — shows modal if guest
function reactGated(articleId, type, btn) {
  if (isGuest()) {
    showGuestModal('Please sign in to react to this article.');
    return;
  }
  react(articleId, type, btn);
}

// ── Navbar User Indicator ──
// Adds a small indicator to navbar showing login status
document.addEventListener('DOMContentLoaded', function() {
  const navbar = document.querySelector('.navbar');
  if (!navbar) return;

  const indicator = document.createElement('div');
  indicator.style.cssText = `
    display: flex; align-items: center; gap: 8px;
    font-family: 'Poppins', sans-serif; font-size: 12px; font-weight: 600;
  `;

  if (isGuest()) {
    indicator.innerHTML = `
      <span style="color:#666;">Browsing as Guest</span>
      <a href="login.html" style="
        background:#FFD700; color:#131045; border-radius:50px;
        padding:6px 16px; text-decoration:none; font-weight:700;
        font-size:12px; transition:background 0.2s;
      ">Sign In</a>
    `;
  } else {
    const studentNum = localStorage.getItem('sors_studentNumber') || '';
    const fullName = localStorage.getItem('sors_fullName') || studentNum || 'UEP User';
    const role = localStorage.getItem('sors_role') || '';

    let navLinks = '';
    if (role === 'ROLE_PIO' || role === 'PIO') {
  navLinks = `
    <a href="pio-dashboard.html" style="
      font-family:'Poppins',sans-serif; font-size:12px; font-weight:600;
      color:#131045; text-decoration:none; padding:5px 14px;
      border:1px solid #e2e2e8; border-radius:50px; transition:all 0.2s;
    ">My Org</a>
    <a href="post-article.html" style="
      font-family:'Poppins',sans-serif; font-size:12px; font-weight:600;
      color:#131045; text-decoration:none; padding:5px 14px;
      border:1px solid #e2e2e8; border-radius:50px; transition:all 0.2s;
    ">Post Article</a>
  `;
} else if (role === 'ROLE_EDITOR_IN_CHIEF' || role === 'EDITOR_IN_CHIEF') {
  navLinks = `
    <a href="post-article.html" style="
      font-family:'Poppins',sans-serif; font-size:12px; font-weight:600;
      color:#131045; text-decoration:none; padding:5px 14px;
      border:1px solid #e2e2e8; border-radius:50px; transition:all 0.2s;
    ">Post Article</a>
  `;
} else {
  navLinks = `
    <a href="apphistory.html" style="
      font-family:'Poppins',sans-serif; font-size:12px; font-weight:600;
      color:#131045; text-decoration:none; padding:5px 14px;
      border:1px solid #e2e2e8; border-radius:50px; transition:all 0.2s;
    ">My Applications</a>
  `;
}

indicator.innerHTML = `
  <span style="color:#131045;">👤 ${fullName}</span>
  ${navLinks}
  <button onclick="logout()" style="
    background:transparent; color:#666; border:1px solid #e2e2e8;
    border-radius:50px; padding:5px 14px; cursor:pointer;
    font-family:'Poppins',sans-serif; font-size:12px; font-weight:600;
    transition:all 0.2s;
  ">Log Out</button>
`;
  }

  navbar.appendChild(indicator);
});

// ── Logout ──
function logout() {
  localStorage.removeItem('sors_user');
  localStorage.removeItem('sors_studentNumber');
  localStorage.removeItem('sors_token');
  localStorage.removeItem('sors_role');
  window.location.href = 'login.html';
}