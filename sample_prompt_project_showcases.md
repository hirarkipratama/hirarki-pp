Featured Engineering Case Studies

Implement the Featured Engineering Case Studies section using the Stitch design as the visual source of truth.

Important:
Most project visuals are MOBILE APPLICATION SCREENSHOTS, not generic landscape thumbnails.

Do NOT crop portrait mobile screenshots into a fixed landscape aspect ratio.

The project media component must support:
- One mobile screenshot.
- Multiple mobile screenshots.
- Portrait screenshots displayed with contain/fit behavior.
- Optional screenshot gallery/rail.
- Responsive presentation between desktop and mobile.

Recommended behavior:

Desktop:
- Featured project may use a larger/full-width card.
- Multiple screenshots may be displayed together in the media area.
- Smaller projects may use a 2-column card grid.
- Preserve the portrait aspect ratio of mobile screenshots.
- Use the card background and subtle gradient/glow from the Stitch design to frame screenshots.

Mobile:
- Stack project cards vertically.
- Preserve screenshot aspect ratios.
- For multiple screenshots, use a horizontally scrollable screenshot rail or another touch-friendly gallery.
- Do not force portrait screenshots into landscape crops.

Project data should support:

Project:
- slug
- title
- category
- summary
- problem
- solution
- impact
- screenshots[]
- technologies[]
- metrics[]
- optional caseStudyUrl

Screenshot:
- image
- alt
- optional caption

Do not invent:
- Project names
- Client names
- Metrics
- App Store / Play Store links
- Screenshots
- Business outcomes

Only render optional fields when content exists.

Accessibility:
- Every informative screenshot requires meaningful alt text.
- Decorative images must not be announced unnecessarily.
- Screenshot galleries must be keyboard accessible where interactive.
- Touch targets must be large enough.
- Do not make image animation the only way to understand project information.

Performance:
- Optimize project screenshots before shipping.
- Use appropriate image dimensions and modern formats.
- Avoid loading every full-resolution screenshot immediately.
- Lazy-load below-the-fold project media where supported.
- Avoid excessive visual effects.

Visual direction:
- Preserve the dark technical card style from Stitch.
- Use cyan/pink accents and subtle ambient glow.
- The screenshot itself should remain the visual focal point.
- Do not let decorative gradients obscure UI screenshots.

Before implementing:
1. Inspect the current Stitch desktop and mobile references.
2. Determine how the screenshot media area behaves at different widths.
3. Define the ProjectCard and ProjectScreenshot composables.
4. Define responsive behavior.
5. Then implement.

After implementation:
- Test at 360px, 390px, 768px, 1024px, and 1440px.
- Verify no screenshot is unintentionally cropped.
- Verify multiple screenshots do not cause horizontal page overflow.
- Verify keyboard/touch interaction if a gallery is implemented.