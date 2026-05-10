export function HeroGraphic() {
  return (
    <svg
      className="hero-graphic"
      viewBox="0 0 560 420"
      xmlns="http://www.w3.org/2000/svg"
      aria-hidden="true"
    >
      <defs>
        <pattern id="grid" width="28" height="28" patternUnits="userSpaceOnUse">
          <path d="M28 0H0V28" fill="none" stroke="currentColor" strokeWidth="0.35" opacity="0.35" />
        </pattern>
      </defs>
      <rect width="560" height="420" fill="url(#grid)" />
      <g stroke="currentColor" fill="none" strokeWidth="1.25" strokeLinecap="round" strokeLinejoin="round">
        <path d="M120 320 L280 140 L440 320 Z" opacity="0.9" />
        <path d="M160 300 L280 170 L400 300" opacity="0.55" />
        <circle cx="280" cy="140" r="10" />
        <circle cx="120" cy="320" r="8" />
        <circle cx="440" cy="320" r="8" />
        <path d="M280 140 V90 M280 90 L340 60 M280 90 L220 60" opacity="0.8" />
        <circle cx="340" cy="60" r="5" />
        <circle cx="220" cy="60" r="5" />
        <path d="M60 200 H140 M420 200 H500 M200 380 H360" opacity="0.45" />
        <rect x="380" y="72" width="120" height="88" rx="4" opacity="0.5" />
        <path d="M405 108 H475 M405 125 H455" opacity="0.45" />
      </g>
    </svg>
  )
}
