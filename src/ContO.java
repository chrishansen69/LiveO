
// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)
// Source File Name:  ContO.java

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

final class ContO {

    public int g_div;
    public int g_idiv;
    public int g_iwid;
    public int g_scalex;
    public int g_scaley;
    public int g_scalez;

    private Plane p[];

    private int npl;
    int x;
    int y;
    int z;
    int xz;
    int xy;
    int zy;
    int wxz;
    int dist;
    int maxR;
    private int disp;
    private boolean shadow;
    private boolean stonecold;
    private int grounded;
    private int rcol;
    private int pcol;
    private int track;
    private boolean out;

    public ContO(BufferedReader datainputstream, final int i, final int j, final int k) throws Exception {

        npl = 0;
        x = 0;
        y = 0;
        z = 0;
        xz = 0;
        xy = 0;
        zy = 0;
        wxz = 0;
        dist = 0;
        maxR = 0;
        disp = 0;
        shadow = false;
        stonecold = false;
        grounded = 1;
        rcol = 0;
        pcol = 0;
        track = -2;
        out = false;
        p = new Plane[0x186a0];
        x = i;
        y = j;
        z = k;
        boolean flag = false;
        int nPoints = 0;
        float div = 1.0F;
        final int pointX[] = new int[350];
        final int pointY[] = new int[350];
        final int pointZ[] = new int[350];
        final int color[] = new int[3];
        boolean glass = false;
        final Wheels wheels = new Wheels();
        int gr = 1;
        int fs = 0;

        float wid = 1.0F;
        boolean hidepoly = false;
        boolean randomcolor = false;
        boolean randoutline = false;
        byte light = 0;

        boolean customstroke = false;
        int strokewidth = 1;
        int strokecap = BasicStroke.CAP_BUTT;
        int strokejoin = BasicStroke.JOIN_MITER;
        int strokemtlimit = 10;

        final float nfmmScale[] = {
                1.0F, 1.0F, 1.0F
        };
        
        boolean glow = false;
        
        int curLine = 0;
        int pStartLine = 0;
        // implicit: curLine on Plane creation, which is </p>
        //int pEndLine = 0;

        String s1;

        //final boolean hasTracks = false;
        //final boolean inTrack = false;

        try {
            //final File fl = new File(s);
            //System.out.println(s.getPath());
            //final DataInputStream datainputstream = new DataInputStream(new FileInputStream(s));
            while (true) {
                if ((s1 = datainputstream.readLine()) == null)
                    break;
                curLine++;
                
                final String line = new StringBuilder().append(s1.trim()).toString();
                if (line.startsWith("<p>") && RunApp.showModel) {
                    flag = true;
                    nPoints = 0;
                    gr = 0;
                    fs = 0;
                    light = 0;
                    hidepoly = false;
                    randomcolor = false;
                    randoutline = false;
                    customstroke = false;
                    strokewidth = 1;
                    strokecap = BasicStroke.CAP_BUTT;
                    strokejoin = BasicStroke.JOIN_MITER;
                    strokemtlimit = 10;
                    glow = false;
                    pStartLine = curLine;
                }
                if (flag) {
                    if (line.startsWith("gr("))
                        gr = getvalue("gr", line, 0);
                    if (line.startsWith("fs("))
                        fs = getvalue("fs", line, 0);
                    if (line.startsWith("glass"))
                        glass = true;
                    if (line.startsWith("c(")) {
                        if (glass)
                            glass = false;
                        color[0] = getvalue("c", line, 0);
                        color[1] = getvalue("c", line, 1);
                        color[2] = getvalue("c", line, 2);
                    }
                    if (line.startsWith("p(")) {
                        pointX[nPoints] = (int) ((int) (getvalue("p", line, 0) * div * wid) * nfmmScale[0]);
                        pointY[nPoints] = (int) ((int) (getvalue("p", line, 1) * div) * nfmmScale[1]);
                        pointZ[nPoints] = (int) ((int) (getvalue("p", line, 2) * div) * nfmmScale[2]);
                        final int maxKeks = (int) Math.sqrt(pointX[nPoints] * pointX[nPoints]
                                + pointY[nPoints] * pointY[nPoints] + pointZ[nPoints] * pointZ[nPoints]);
                        if (maxKeks > maxR)
                            //System.out.println(maxKeks);
                            maxR = maxKeks;
                        nPoints++;
                    }
                    if (line.startsWith("random()") || line.startsWith("rainbow()"))
                        randomcolor = true;
                    if (line.startsWith("randoutline()"))
                        randoutline = true;
                    if (line.startsWith("lightF"))
                        light = 1;
                    if (line.startsWith("lightB"))
                        light = 2;
                    if (line.startsWith("light()"))
                        light = 1;
                    if (line.startsWith("noOutline()"))
                        hidepoly = true;
                    if (line.startsWith("customOutline"))
                        customstroke = true;
                    if (line.startsWith("$outlineW("))
                        strokewidth = getvalue("$outlineW", line, 0);
                    if (line.startsWith("$outlineCap(")) {
                        if (line.startsWith("$outlineCap(butt)"))
                            strokecap = BasicStroke.CAP_BUTT;
                        if (line.startsWith("$outlineCap(round)"))
                            strokecap = BasicStroke.CAP_ROUND;
                        if (line.startsWith("$outlineCap(square)"))
                            strokecap = BasicStroke.CAP_SQUARE;
                    }
                    if (line.startsWith("$outlineJoin(")) {
                        if (line.startsWith("$outlineJoin(bevel)"))
                            strokejoin = BasicStroke.JOIN_BEVEL;
                        if (line.startsWith("$outlineJoin(miter)"))
                            strokejoin = BasicStroke.JOIN_MITER;
                        if (line.startsWith("$outlineJoin(round)"))
                            strokejoin = BasicStroke.JOIN_ROUND;
                    }
                    if (line.startsWith("$outlineMtlimit("))
                        strokemtlimit = getvalue("$outlineMtlimit", line, 0);
                    if (line.startsWith("$glow()") || line.startsWith("$gl()"))
                        glow = true;

                }
                if (line.startsWith("</p>") && RunApp.showModel) {
                    p[npl] = new Plane(pointX, pointZ, pointY, nPoints, color, glass, gr, fs, 0, 0, light, hidepoly,
                            randomcolor, randoutline, customstroke, strokewidth, strokecap, strokejoin, strokemtlimit, glow);
                    p[npl].startLine = pStartLine;
                    p[npl].endLine = curLine;
                    npl++;
                    flag = false;
                }
                if (line.startsWith("w") && RunApp.showModel)
                    npl += wheels.make(p, npl, (int) (getvalue("w", line, 0) * div * nfmmScale[0]),
                            (int) (getvalue("w", line, 1) * div * nfmmScale[1]),
                            (int) (getvalue("w", line, 2) * div * nfmmScale[2]), getvalue("w", line, 3),
                            (int) (getvalue("w", line, 4) * div * nfmmScale[0]), (int) (getvalue("w", line, 5) * div));
                        //npl += wheels.make(applet, m, p, npl, (int)((float)getvalue("w", s1, 0) * f * f1 * nfmm_scale[0]), (int)((float)getvalue("w", s1, 1) * f * nfmm_scale[1]), (int)((float)getvalue("w", s1, 2) * f * nfmm_scale[2]), getvalue("w", s1, 3), (int)((float)getvalue("w", s1, 4) * f * f1), (int)((int)getvalue("w", s1, 5) * f), i1);

                /*if (s2.startsWith("<track>"))
                  track = -1;
                if (track == -1) {
                  if (s2.startsWith("c")) {
                    m.tr.c[m.tr.nt][0] = getvalue("c", s2, 0);
                    m.tr.c[m.tr.nt][1] = getvalue("c", s2, 1);
                    m.tr.c[m.tr.nt][2] = getvalue("c", s2, 2);
                  }
                  if (s2.startsWith("xy"))
                    m.tr.xy[m.tr.nt] = getvalue("xy", s2, 0);
                  if (s2.startsWith("zy"))
                    m.tr.zy[m.tr.nt] = getvalue("zy", s2, 0);
                  if (s2.startsWith("radx"))
                    m.tr.radx[m.tr.nt] = (int) (getvalue("radx", s2, 0) * f);
                  if (s2.startsWith("radz"))
                    m.tr.radz[m.tr.nt] = (int) (getvalue("radz", s2, 0) * f);
                }
                if (s2.startsWith("</track>")) {
                  track = m.tr.nt;
                  m.tr.nt++;
                }*/
                if (Trackers.nt + 1 > Trackers.xy.length)
                    throw new RuntimeException("increase tracks()");
                if (line.startsWith("<track>")) {
                    Trackers.notwall[Trackers.nt] = false;
                    Trackers.dam[Trackers.nt] = 1;
                    Trackers.skd[Trackers.nt] = 0;
                    Trackers.y[Trackers.nt] = 0;
                    Trackers.x[Trackers.nt] = 0;
                    Trackers.z[Trackers.nt] = 0;
                    Trackers.xy[Trackers.nt] = 0;
                    Trackers.zy[Trackers.nt] = 0;
                    Trackers.rady[Trackers.nt] = 0;
                    Trackers.radx[Trackers.nt] = 0;
                    Trackers.radz[Trackers.nt] = 0;
                    Trackers.c[Trackers.nt][0] = 0;
                    Trackers.c[Trackers.nt][1] = 0;
                    Trackers.c[Trackers.nt][2] = 0;
                    track = -1;
                }
                if (track == -1) {
                    if (line.startsWith("c")) {
                        Trackers.c[Trackers.nt][0] = getvalue("c", line, 0);
                        Trackers.c[Trackers.nt][1] = getvalue("c", line, 1);
                        Trackers.c[Trackers.nt][2] = getvalue("c", line, 2);
                    }
                    if (line.startsWith("xy"))
                        Trackers.xy[Trackers.nt] = getvalue("xy", line, 0);
                    if (line.startsWith("zy"))
                        Trackers.zy[Trackers.nt] = getvalue("zy", line, 0);
                    if (line.startsWith("radx"))
                        Trackers.radx[Trackers.nt] = (int) (getvalue("radx", line, 0) * div);
                    if (line.startsWith("rady"))
                        Trackers.rady[Trackers.nt] = (int) (getvalue("rady", line, 0) * div);
                    if (line.startsWith("radz"))
                        Trackers.radz[Trackers.nt] = (int) (getvalue("radz", line, 0) * div);
                    if (line.startsWith("ty"))
                        Trackers.y[Trackers.nt] = (int) (getvalue("ty", line, 0) * div);
                    if (line.startsWith("tx"))
                        Trackers.x[Trackers.nt] = (int) (getvalue("tx", line, 0) * div);
                    if (line.startsWith("tz"))
                        Trackers.z[Trackers.nt] = (int) (getvalue("tz", line, 0) * div);
                    if (line.startsWith("skid"))
                        Trackers.skd[Trackers.nt] = getvalue("skid", line, 0);
                    if (line.startsWith("dam"))
                        Trackers.dam[Trackers.nt] = 3;
                    if (line.startsWith("notwall"))
                        Trackers.notwall[Trackers.nt] = true;
                }
                if (line.startsWith("</track>")) {
                    //

                    final int x1 = Trackers.x[Trackers.nt] - Trackers.radx[Trackers.nt];
                    final int x2 = Trackers.x[Trackers.nt] + Trackers.radx[Trackers.nt];
                    final int y1 = Trackers.y[Trackers.nt] - Trackers.rady[Trackers.nt];
                    final int y2 = Trackers.y[Trackers.nt] + Trackers.rady[Trackers.nt];
                    final int z1 = Trackers.z[Trackers.nt] - Trackers.radz[Trackers.nt];
                    final int z2 = Trackers.z[Trackers.nt] + Trackers.radz[Trackers.nt];

                    /*
                     * x = 200
                     * y = 100
                     * z = 300
                     *
                    <p>
                    p(-x,y,z)
                    p(-x,y,-z)
                    p(-x,-y,-z)
                    p(-x,-y,z) // human eyes careful
                    </p>

                    <p>
                    p(x,-y,z)
                    p(x,y,z)
                    p(-x,y,z)
                    p(-x,-y,z)
                    </p>

                    <p>
                    p(x,-y,z)
                    p(x,y,z) // human eyes careful
                    p(x,y,-z)
                    p(x,-y,-z)
                    </p>

                    <p>
                    p(x,y,-z)
                    p(-x,y,-z)
                    p(-x,-y,-z)
                    p(x,-y,-z)
                    </p>
                    */

                    final int ggr = 0;
                    //if (RunApp.solidsApproachScreen)
                    //    ggr = -51;
                    if (RunApp.showSolids) {
                        final int[] pc = {
                                255, 0, 0
                        };
                        final int[] px = {
                                x1, x1, x1, x1,
                        };
                        final int[] py = {
                                y2, y2, y1, y1,
                        };
                        final int[] pz = {
                                z2, z1, z1, z2,
                        };

                        p[npl] = new Plane(px, pz, py, 4, pc, false, ggr, 0, 0, 0, (byte) 0, false,
                                false /*rndcolor*/, false, false, 0, 0, 0, 0);
                        npl++;

                        final int[] apc = {
                                0, 255, 0
                        };
                        final int[] apx = {
                                x2, x2, x1, x1,
                        };
                        final int[] apy = {
                                y1, y2, y2, y1,
                        };
                        final int[] apz = {
                                z2, z2, z2, z2,
                        };
                        p[npl] = new Plane(apx, apz, apy, 4, apc, false, ggr, 0, 0, 0, (byte) 0, false,
                                false /*rndcolor*/, false, false, 0, 0, 0, 0);
                        npl++;

                        final int[] bpc = {
                                0, 0, 255
                        };
                        final int[] bpx = {
                                x2, x2, x2, x2,
                        };
                        final int[] bpy = {
                                y1, y2, y2, y1,
                        };
                        final int[] bpz = {
                                z2, z2, z1, z1,
                        };
                        p[npl] = new Plane(bpx, bpz, bpy, 4, bpc, false, ggr, 0, 0, 0, (byte) 0, false,
                                false /*rndcolor*/, false, false, 0, 0, 0, 0);
                        npl++;

                        final int[] cpc = {
                                255, 255, 255
                        };
                        final int[] cpx = {
                                x2, x1, x1, x2,
                        };
                        final int[] cpy = {
                                y2, y2, y1, y1,
                        };
                        final int[] cpz = {
                                z1, z1, z1, z1,
                        };
                        p[npl] = new Plane(cpx, cpz, cpy, 4, cpc, false, ggr, 0, 0, 0, (byte) 0, false,
                                false /*rndcolor*/, false, false, 0, 0, 0, 0);
                        npl++;
                    }

                    /* Track Flats / Faces */
                    /* Captures RadX and RadZ, RadY can be interpreted/determined by model */
                    if (RunApp.showTrackFaces)
                        if ((Trackers.zy[Trackers.nt] == 90 || Trackers.zy[Trackers.nt] == -90) && Trackers.xy[Trackers.nt] == 0) {
                            final int[] px = {
                                    Trackers.x[Trackers.nt] - Trackers.radx[Trackers.nt], Trackers.x[Trackers.nt] - Trackers.radx[Trackers.nt],
                                    Trackers.x[Trackers.nt] + Trackers.radx[Trackers.nt], Trackers.x[Trackers.nt] + Trackers.radx[Trackers.nt]
                            };
                            final int[] py = {
                                    Trackers.y[Trackers.nt], Trackers.y[Trackers.nt], Trackers.y[Trackers.nt], Trackers.y[Trackers.nt]
                            };
                            final int[] pz = {
                                    Trackers.z[Trackers.nt] - Trackers.rady[Trackers.nt], Trackers.z[Trackers.nt] + Trackers.rady[Trackers.nt],
                                    Trackers.z[Trackers.nt] + Trackers.rady[Trackers.nt], Trackers.z[Trackers.nt] - Trackers.rady[Trackers.nt]
                            }; // may need changing
                            final int[] pc = {
                                    255, 255, 0
                            };

                            Plane.rot(py, pz, Trackers.y[Trackers.nt], Trackers.z[Trackers.nt], -Trackers.zy[Trackers.nt], 4);

                            p[npl] = new Plane(px, pz, py, 4, pc, false, 0, 0, 0, 0, (byte) 0, false,
                                    false /*rndcolor*/, false, false, 0, 0, 0, 0);
                            npl++;
                        } else if ((Trackers.xy[Trackers.nt] == 90 || Trackers.xy[Trackers.nt] == -90) && Trackers.zy[Trackers.nt] == 0) {
                            final int[] px = {
                                    Trackers.x[Trackers.nt] - Trackers.rady[Trackers.nt], Trackers.x[Trackers.nt] - Trackers.rady[Trackers.nt],
                                    Trackers.x[Trackers.nt] + Trackers.rady[Trackers.nt], Trackers.x[Trackers.nt] + Trackers.rady[Trackers.nt]
                            };
                            final int[] py = {
                                    Trackers.y[Trackers.nt], Trackers.y[Trackers.nt], Trackers.y[Trackers.nt], Trackers.y[Trackers.nt]
                            };
                            final int[] pz = {
                                    Trackers.z[Trackers.nt] - Trackers.radz[Trackers.nt], Trackers.z[Trackers.nt] + Trackers.radz[Trackers.nt],
                                    Trackers.z[Trackers.nt] + Trackers.radz[Trackers.nt], Trackers.z[Trackers.nt] - Trackers.radz[Trackers.nt]
                            }; // may need changing
                            final int[] pc = {
                                    255, 255, 0
                            };

                            Plane.rot(py, px, Trackers.y[Trackers.nt], Trackers.x[Trackers.nt], -Trackers.xy[Trackers.nt], 4);

                            p[npl] = new Plane(px, pz, py, 4, pc, false, 0, 0, 0, 0, (byte) 0, false,
                                    false /*rndcolor*/, false, false, 0, 0, 0, 0);
                            npl++;
                        } else {
                            final int[] px = {
                                    Trackers.x[Trackers.nt] - Trackers.radx[Trackers.nt], Trackers.x[Trackers.nt] - Trackers.radx[Trackers.nt],
                                    Trackers.x[Trackers.nt] + Trackers.radx[Trackers.nt], Trackers.x[Trackers.nt] + Trackers.radx[Trackers.nt]
                            };
                            final int[] py = {
                                    Trackers.y[Trackers.nt], Trackers.y[Trackers.nt], Trackers.y[Trackers.nt], Trackers.y[Trackers.nt]
                            };
                            final int[] pz = {
                                    Trackers.z[Trackers.nt] - Trackers.radz[Trackers.nt], Trackers.z[Trackers.nt] + Trackers.radz[Trackers.nt],
                                    Trackers.z[Trackers.nt] + Trackers.radz[Trackers.nt], Trackers.z[Trackers.nt] - Trackers.radz[Trackers.nt]
                            }; // may need changing
                            final int[] pc = {
                                    255, 255, 0
                            };

                            Plane.rot(py, px, Trackers.y[Trackers.nt], Trackers.x[Trackers.nt], -Trackers.xy[Trackers.nt], 4);
                            Plane.rot(py, pz, Trackers.y[Trackers.nt], Trackers.z[Trackers.nt], -Trackers.zy[Trackers.nt], 4);

                            p[npl] = new Plane(px, pz, py, 4, pc, false, 0, 0, 0, 0, (byte) 0, false,
                                    false /*rndcolor*/, false, false, 0, 0, 0, 0);
                            npl++;
                        }
                    track = Trackers.nt;
                    Trackers.nt++;
                }
                Trackers.prepare();

                if (line.startsWith("MaxRadius"))
                    maxR = (int) (getvalue("MaxRadius", line, 0) * div);
                if (line.startsWith("disp"))
                    disp = getvalue("disp", line, 0);
                if (line.startsWith("shadow"))
                    shadow = true;
                if (line.startsWith("out"))
                    out = true;
                if (line.startsWith("colid")) {
                    rcol = getvalue("colid", line, 0);
                    pcol = getvalue("colid", line, 1);
                }
                if (line.startsWith("grounded"))
                    grounded = getvalue("grounded", line, 0);
                if (line.startsWith("div")) {
                    div = getvalue("div", line, 0) / 10F;
                    g_div = getvalue("div", line, 0);
                }
                if (line.startsWith("idiv")) {
                    div = getvalue("idiv", s1, 0) / 100F;
                    g_idiv = getvalue("idiv", line, 0);
                }
                if (line.startsWith("iwid")) {
                    wid = getvalue("iwid", s1, 0) / 100F;
                    g_iwid = getvalue("iwid", line, 0);
                }
                if (line.startsWith("ScaleX")) {
                    nfmmScale[0] = getvalue("ScaleX", s1, 0) / 100F;
                    g_scalex = getvalue("ScaleX", line, 0);
                }
                if (line.startsWith("ScaleY")) {
                    nfmmScale[1] = getvalue("ScaleY", s1, 0) / 100F;
                    g_scaley = getvalue("ScaleY", line, 0);
                }
                if (line.startsWith("ScaleZ")) {
                    nfmmScale[2] = getvalue("ScaleZ", s1, 0) / 100F;
                    g_scalez = getvalue("ScaleZ", line, 0);
                }
                if (line.startsWith("stonecold"))
                    stonecold = true;
            }
            datainputstream.close();
        } catch (final Exception exception) {
            throw exception;
        }
        System.out.println(new StringBuilder().append("polygantos: ").append(npl).toString());
        p[npl - 1].imlast = true;
    
    }

    public ContO(final DataInputStream s, final int i, final int j, final int k) throws Exception {
        this(new BufferedReader(new InputStreamReader(s)), i, j, k);
    }

    public ContO(final String s, final int i, final int j, final int k) throws Exception {
        this(new BufferedReader(new StringReader(s)), i, j, k);
    }

    public void d(final Graphics g) {
        if (dist != 0)
            dist = 0;
        //if (track != -2)
        //	m.tr.in[track] = false;
        if (!out) {
            //System.out.println(maxR);
            final int i = Medium.cx + (int) ((x - Medium.x - Medium.cx) * Math.cos(Medium.xz * 0.017453292519943295D)
                    - (z - Medium.z - Medium.cz) * Math.sin(Medium.xz * 0.017453292519943295D));
            final int j = Medium.cz + (int) ((x - Medium.x - Medium.cx) * Math.sin(Medium.xz * 0.017453292519943295D)
                    + (z - Medium.z - Medium.cz) * Math.cos(Medium.xz * 0.017453292519943295D));
            final int k = Medium.cz + (int) ((y - Medium.y - Medium.cy) * Math.sin(Medium.zy * 0.017453292519943295D)
                    + (j - Medium.cz) * Math.cos(Medium.zy * 0.017453292519943295D));
            if (xs(i + maxR, k) > 0 && xs(i - maxR, k) < Medium.w && k > -maxR && k < Medium.fade[7]
                    && xs(i + maxR, k) - xs(i - maxR, k) > disp || Medium.infiniteDistance) {
                if (shadow) {
                    final int l = Medium.cy + (int) ((Medium.ground - Medium.cy) * Math.cos(Medium.zy * 0.017453292519943295D)
                            - (j - Medium.cz) * Math.sin(Medium.zy * 0.017453292519943295D));
                    final int j1 = Medium.cz + (int) ((Medium.ground - Medium.cy) * Math.sin(Medium.zy * 0.017453292519943295D)
                            + (j - Medium.cz) * Math.cos(Medium.zy * 0.017453292519943295D));
                    if (ys(l + maxR, j1) > 0 && ys(l - maxR, j1) < Medium.h)
                        for (int k1 = 0; k1 < npl; k1++)
                            p[k1].s(g, x - Medium.x, y - Medium.y, z - Medium.z, xz, xy, zy);
                }
                final int i1 = Medium.cy + (int) ((y - Medium.y - Medium.cy) * Math.cos(Medium.zy * 0.017453292519943295D)
                        - (j - Medium.cz) * Math.sin(Medium.zy * 0.017453292519943295D));
                if (ys(i1 + maxR, k) > 0 && ys(i1 - maxR, k) < Medium.h) {
                    final int ai[] = new int[npl];
                    for (int l1 = 0; l1 < npl; l1++) {
                        ai[l1] = 0;
                        for (int j2 = 0; j2 < npl; j2++)
                            if (p[l1].av != p[j2].av) {
                                if (p[l1].av < p[j2].av)
                                    ai[l1]++;
                            } else if (l1 > j2)
                                ai[l1]++;

                    }

                    LiveO.mouseInPoly = -1;
                    LiveO.mouseInPoint = -1;
                    for (int i2 = 0; i2 < npl; i2++)
                        for (int k2 = 0; k2 < npl; k2++)
                            if (ai[k2] == i2) {
                                if (LiveO.trans && p[k2].glass)
                                    ((Graphics2D) g).setComposite(AlphaComposite.getInstance(3, 0.7F));
                                p[k2].d((Graphics2D) g, x - Medium.x, y - Medium.y, z - Medium.z, xz, xy, zy, wxz, stonecold, k2);
                                if (LiveO.trans && p[k2].glass)
                                    ((Graphics2D) g).setComposite(AlphaComposite.getInstance(3, 1.0F));
                            }
                    try {
                        highlight();
                    } catch (IOException | BadLocationException e) {
                        e.printStackTrace();
                    }

                    dist = (int) Math.sqrt((int) Math.sqrt((Medium.x + Medium.cx - x) * (Medium.x + Medium.cx - x) + (Medium.z - z) * (Medium.z - z)
                            + (Medium.y + Medium.cy - y) * (Medium.y + Medium.cy - y))) * grounded;
                }
            }
        }
        /*if (dist != 0 && track != -2) {
        	m.tr.in[track] = true;
        	m.tr.x[track] = x - m.x;
        	m.tr.y[track] = y - m.y;
        	m.tr.z[track] = z - m.z;
        }*/
    }

    private void highlight() throws IOException, BadLocationException {
        if (LiveO.mouseInPoly != -1) {
            //System.err.println("found it!");
            if (LiveO.mouses == LiveO.MOUSE_PRESSED) {
                int startLine = p[LiveO.mouseInPoly].startLine-1;
                int endLine = p[LiveO.mouseInPoly].endLine;

                int startIndex = RunApp.t.text.getLineStartOffset(startLine);
                int endIndex = RunApp.t.text.getLineEndOffset(endLine);
                
                System.err.println("clicky!");
            
                RunApp.t.text.select(startIndex, endIndex);
                Highlighter.HighlightPainter p = new DefaultHighlighter.DefaultHighlightPainter(Color.pink);
                RunApp.t.text.getHighlighter().removeAllHighlights();
                RunApp.t.text.getHighlighter().addHighlight(startIndex, endIndex, p);
            }
        }
    }

    private void reset() {
        xz = 0;
        xy = 0;
        zy = 0;
    }

    private int xs(final int i, int j) {
        if (j < 10)
            j = 10;
        return (j - Medium.focus_point) * (Medium.cx - i) / j + i;
    }

    private int ys(final int i, int j) {
        if (j < 10)
            j = 10;
        return (j - Medium.focus_point) * (Medium.cy - i) / j + i;
    }

    int getvalue(final String s, final String s1, final int i) {
        int k = 0;
        String s3 = "";
        for (int j = s.length() + 1; j < s1.length(); j++) {
            final String s2 = new StringBuilder().append("").append(s1.charAt(j)).toString();
            if (s2.equals(",") || s2.equals(")")) {
                k++;
                j++;
            }
            if (k == i)
                s3 = new StringBuilder().append(s3).append(s1.charAt(j)).toString();
        }

        return Integer.valueOf(s3).intValue();
    }

    private int getpy(final int i, final int j, final int k) {
        return (i - x) / 10 * ((i - x) / 10) + (j - y) / 10 * ((j - y) / 10) + (k - z) / 10 * ((k - z) / 10);
    }

    public int[] centroid() {
        int amt = 0;
        for (int i = 0; i < npl; i++) {
            amt += p[i].n;
        }
        int x = 0;
        int y = 0;
        int z = 0;
        
        for (int i = 0; i < npl; i++) {
            for (int j = 0; j < p[i].n; j++) {
                x += p[i].x[j];
                y += p[i].y[j];
                z += p[i].z[j];
            }
        }

        x /= amt;
        y /= amt;
        z /= amt;
        return new int[] {x, y, z};
    }
}
