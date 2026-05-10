export function Footer() {
  return (
    <footer className="footer">
      <div className="footer__row">
        <p className="footer__brand">Vector Atelier</p>
        <ul className="footer__links">
          <li>
            <a href="#top">Privacy</a>
          </li>
          <li>
            <a href="#top">Terms</a>
          </li>
          <li>
            <a href="mailto:hello@example.com">hello@example.com</a>
          </li>
        </ul>
      </div>
      <p className="footer__legal">
        © {new Date().getFullYear()} Vector Atelier Ltd. All rights reserved. Dummy contact details
        for demonstration.
      </p>
    </footer>
  )
}
